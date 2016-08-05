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
import com.orhanobut.logger.Logger;

import android.content.Context;

import java.util.ArrayList;
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
                    museDataListenerWrapper.addMuseDataPacketSubscriber(subscriber);

                    muse.registerDataListener(museDataListenerWrapper, MuseDataPacketType.BATTERY);
                    muse.registerDataListener(museDataListenerWrapper, MuseDataPacketType
                            .HSI_PRECISION);
                    muse.registerDataListener(museDataListenerWrapper, MuseDataPacketType.EEG);

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
                    museDataListenerWrapper.addMuseArtifactPacketSubscriber(subscriber);
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

        public List<Subscriber> museDataPacketSubscribers;
        public List<Subscriber> museArtifactPacketSubscribers;


        @Override
        public void receiveMuseDataPacket(MuseDataPacket museDataPacket, Muse muse) {
            if (museDataPacketSubscribers != null) {
                for (Subscriber subscriber : museDataPacketSubscribers) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(museDataPacket);
                    } else {
                        Logger.t(TAG).d("remove the subscriber ");
                        museDataPacketSubscribers.remove(subscriber);
                    }
                }
            }
        }

        @Override
        public void receiveMuseArtifactPacket(MuseArtifactPacket museArtifactPacket, Muse muse) {
            if (museArtifactPacketSubscribers != null) {
                for (Subscriber subscriber : museArtifactPacketSubscribers) {
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onNext(museArtifactPacket);
                    } else {
                        museArtifactPacketSubscribers.remove(subscriber);
                    }
                }
            }

        }

        public void addMuseDataPacketSubscriber(Subscriber<? super MuseDataPacket>
                                                        museDataPacketSubscriber) {
            if (museDataPacketSubscribers == null) {
                museDataPacketSubscribers = new ArrayList<>();
            }

            museDataPacketSubscribers.add(museDataPacketSubscriber);
            Logger.t(TAG).d("add new muse data subscriber, new list size " +
                    museDataPacketSubscribers.size());
        }

        public void addMuseArtifactPacketSubscriber(Subscriber<? super MuseArtifactPacket>
                                                            museArtifactPacketSubscriber) {
            if (museArtifactPacketSubscribers == null) {
                museArtifactPacketSubscribers = new ArrayList<>();
            }

            museArtifactPacketSubscribers.add(museArtifactPacketSubscriber);
        }
    }

}
