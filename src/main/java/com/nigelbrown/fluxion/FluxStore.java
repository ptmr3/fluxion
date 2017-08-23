package com.nigelbrown.fluxion;

import com.nigelbrown.fluxion.Annotation.React;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Nigel.Brown on 5/12/2017.
 */
public abstract class FluxStore {

	public FluxStore() {}

	private Reaction newReaction(String reactionId,Object... data) {
		if(reactionId.isEmpty()) {
			throw new IllegalArgumentException("Type must not be empty");
		}
		if(data.length % 2 != 0) {
			throw new IllegalArgumentException("Data must be a valid list of key,value pairs");
		}
		Reaction.Builder reactionBuilder = Reaction.type(reactionId);
		int i = 0;
		while(i < data.length) {
			String key = (String)data[i++];
			Object value = data[i++];
			reactionBuilder.bundle(key, value);
		}
		return reactionBuilder.build();
	}

	protected void emitReaction(String reactionId,Object... data) throws IllegalAccessException,InvocationTargetException {
		if(reactionId.isEmpty()) {
			throw new IllegalArgumentException("Type must not be empty");
		}
		if(data.length % 2 != 0) {
			throw new IllegalArgumentException("Data must be a valid list of key,value pairs");
		}
		Reaction.Builder reactionBuilder = Reaction.type(reactionId);
		int i = 0;
		while(i < data.length) {
			String key = (String)data[i++];
			Object value = data[i++];
			reactionBuilder.bundle(key, value);
		}
		final Reaction reaction = reactionBuilder.build();

		Flux.getsInstance().emitReaction(reaction)
		    .subscribeOn(Schedulers.computation())
		    .observeOn(AndroidSchedulers.mainThread())
		    .subscribe(getReactionObserver());
	}

	Observer getReactionObserver(){
		return new Observer() {
			@Override
			public void onSubscribe(@NonNull Disposable d) {
			}

			@Override
			public void onNext(@NonNull Object o) {
				HashMap<String,Object> map = (HashMap<String, Object>)o;
				Method method = (Method)map.get("METHOD");
				Class<?> parentClass = (Class<?>)map.get("CLASS");
				Reaction reaction = (Reaction)map.get("REACTION");
				try {
					method.invoke(parentClass,reaction);
				}catch(Exception e){

				}
			}

			@Override
			public void onError(@NonNull Throwable e) {
			}

			@Override
			public void onComplete() {
			}
		};

	}
}
