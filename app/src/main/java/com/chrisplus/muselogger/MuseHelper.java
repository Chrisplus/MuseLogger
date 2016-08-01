package com.chrisplus.muselogger;

import com.choosemuse.libmuse.Muse;
import com.choosemuse.libmuse.MuseArtifactPacket;
import com.choosemuse.libmuse.MuseConnectionListener;
import com.choosemuse.libmuse.MuseConnectionPacket;
import com.choosemuse.libmuse.MuseDataListener;
import com.choosemuse.libmuse.MuseDataPacket;
import com.choosemuse.libmuse.MuseDataPacketType;
import com.choosemuse.libmuse.MuseListener;
import com.choosemuse.libmuse.MuseManagerAndroid;

import android.content.Context;

import java.util.List;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by chrisplus on 22/7/16.
 */
public class MuseHelper {
    public static final String TAG = MuseHelper.class.getSimpleName();
    public static final String THROWABLE_NULL_MANAGER = "muse manager instance is null";

    private static MuseHelper instance;

    private MuseManagerAndroid museManager;
    private MuseDataListenerWrapper museDataListenerWrapper;


    private MuseHelper(Context context) {
        museManager = MuseManagerAndroid.getInstance();
        museManager.setContext(context);
        museDataListenerWrapper = new MuseDataListenerWrapper();
    }

    public synchronized static MuseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MuseHelper(context);
        }
        return instance;
    }

    public void startListening() {
        if (museManager != null) {
            museManager.startListening();
        }
    }

    public void stopListening() {
        if (museManager != null) {
            museManager.stopListening();
        }

    }

    public List<Muse> refreshMuse() {
        startListening();
        return museManager.getMuses();
    }

    public Observable<List<Muse>> observeMuseList() {
        startListening();
        return Observable.create(new Observable.OnSubscribe<List<Muse>>() {
            @Override
            public void call(final Subscriber<? super List<Muse>> subscriber) {
                if (museManager != null) {
                    final MuseListener museListener = new MuseListener() {
                        @Override
                        public void museListChanged() {
                            subscriber.onNext(museManager.getMuses());
                            stopListening();
                        }
                    };
                    museManager.setMuseListener(museListener);
                } else {
                    subscriber.onError(new Throwable(THROWABLE_NULL_MANAGER));
                }
            }
        });
    }

    public Observable<MuseConnectionPacket> observeMuseConnectionStatus(final Muse muse) {
        return Observable.create(new Observable.OnSubscribe<MuseConnectionPacket>() {
            @Override
            public void call(final Subscriber<? super MuseConnectionPacket> subscriber) {
                if (museManager != null) {
                    final MuseConnectionListener connectionListener = new MuseConnectionListener() {
                        @Override
                        public void receiveMuseConnectionPacket(MuseConnectionPacket
                                                                        museConnectionPacket,
                                                                Muse muse) {
                            if (subscriber.isUnsubscribed()) {
                                muse.unregisterConnectionListener(this);
                            } else {
                                subscriber.onNext(museConnectionPacket);
                            }
                        }
                    };

                    muse.registerConnectionListener(connectionListener);
                } else {
                    subscriber.onError(new Throwable(THROWABLE_NULL_MANAGER));
                    muse.unregisterAllListeners();
                }
            }
        });
    }


    public Observable<MuseDataPacket> observeMuseData(final Muse muse) {
        return Observable.create(new Observable.OnSubscribe<MuseDataPacket>() {
            @Override
            public void call(Subscriber<? super MuseDataPacket> subscriber) {
                if (museManager != null) {
                    museDataListenerWrapper.setMuseDataPacketSubscriber(subscriber);
                    muse.registerDataListener(museDataListenerWrapper, MuseDataPacketType.BATTERY);
                } else {
                    subscriber.onError(new Throwable(THROWABLE_NULL_MANAGER));
                    muse.unregisterAllListeners();
                }
            }
        });
    }

    public Observable<MuseArtifactPacket> observeMuseArtifactPacket(final Muse muse) {
        return Observable.create(new Observable.OnSubscribe<MuseArtifactPacket>() {
            @Override
            public void call(Subscriber<? super MuseArtifactPacket> subscriber) {
                if (museManager != null) {
                    museDataListenerWrapper.setMuseArtifactPacketSubscriber(subscriber);
                    muse.registerDataListener(museDataListenerWrapper, MuseDataPacketType
                            .ARTIFACTS);
                } else {
                    subscriber.onError(new Throwable(THROWABLE_NULL_MANAGER));
                }
            }
        });
    }


    public class MuseConnectionStatusPacket {
        public MuseConnectionPacket conntectionPacket;
        public Muse muse;

        public MuseConnectionStatusPacket(MuseConnectionPacket connectionPacket, Muse muse) {
            this.conntectionPacket = connectionPacket;
            this.muse = muse;
        }
    }

    public class MuseDataListenerWrapper extends MuseDataListener {

        public Subscriber<? super MuseDataPacket> museDataPacketSubscriber;
        public Subscriber<? super MuseArtifactPacket> museArtifactPacketSubscriber;


        @Override
        public void receiveMuseDataPacket(MuseDataPacket museDataPacket, Muse muse) {
            if (museDataPacketSubscriber != null && !museDataPacketSubscriber.isUnsubscribed()) {
                museDataPacketSubscriber.onNext(museDataPacket);
            }
        }

        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket museArtifactPacket, Muse muse) {
            if (museArtifactPacketSubscriber != null && !museDataPacketSubscriber.isUnsubscribed
                    ()) {
                museArtifactPacketSubscriber.onNext(museArtifactPacket);
            }

        }

        public void setMuseDataPacketSubscriber(Subscriber<? super MuseDataPacket>
                                                        museDataPacketSubscriber) {
            this.museDataPacketSubscriber = museDataPacketSubscriber;
        }

        public void setMuseArtifactPacketSubscriber(Subscriber<? super MuseArtifactPacket>
                                                            museArtifactPacketSubscriber) {
            this.museArtifactPacketSubscriber = museArtifactPacketSubscriber;
        }
    }

}
