package it.unipr.advmobdev.mat301275.facemorph.modules.bluetooth;

import android.graphics.Bitmap;

import java.lang.ref.WeakReference;

import it.unipr.advmobdev.mat301275.facemorph.blemanager.BleCallback;
import it.unipr.advmobdev.mat301275.facemorph.blemanager.BleGuest;
import it.unipr.advmobdev.mat301275.facemorph.blemanager.BleHost;
import it.unipr.advmobdev.mat301275.facemorph.modules.home.HomeFragment;

public class BluetoothController {

    private BluetoothCallback callback;

    private BleHost host = null;
    private BleGuest guest = null;

    private WeakReference<BluetoothFragment> weakFragment = null;

    public BluetoothController(BluetoothFragment fragment) {
        weakFragment = new WeakReference<>(fragment);
    }

    public void setCallback(BluetoothCallback callback) {
        this.callback = callback;
    }

    public void hostBluetooth() {
        host = new BleHost(new BleCallback() {
            @Override
            public void bleSuccess(Bitmap bitmap) {

            }

            @Override
            public void bleFailed(Exception exception) {

            }

            @Override
            public void bleProgress(int progress) {

            }
        }, callback.getBitmap());

        BluetoothFragment fragment = weakFragment.get();
        if (fragment != null) {
            host.start(fragment.getContext());
        }
    }

    public void joinBluetooth() {
        guest = new BleGuest(new BleCallback() {
            @Override
            public void bleSuccess(Bitmap bitmap) {

            }

            @Override
            public void bleFailed(Exception exception) {

            }

            @Override
            public void bleProgress(int progress) {

            }
        }, callback.getBitmap());

        BluetoothFragment fragment = weakFragment.get();
        if (fragment != null) {
            guest.start(fragment.getContext());
        }
    }

    public void cancelClicked() {

    }

    public void viewGone() {
        if (host != null) {
            host.stop();
        }

        if (guest != null) {
            guest.stop();
        }
    }

}
