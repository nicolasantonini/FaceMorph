package it.unipr.advmobdev.mat301275.facemorph.modules.bluetooth;

import android.graphics.Bitmap;
import android.util.Log;

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
                host.stop();
                callback.bleRetrieveSuccess(bitmap);
            }

            @Override
            public void bleFailed(Exception exception) {
                host.stop();
                BluetoothFragment fragment = weakFragment.get();
                if (fragment != null) {
                    fragment.displayToast(exception.toString());
                    fragment.enableInteraction();
                }

            }

            @Override
            public void bleProgress(int progress) {
                BluetoothFragment fragment = weakFragment.get();
                if (fragment != null) {
                    fragment.setProgress(progress);
                }
            }
        }, callback.getBitmap());

        BluetoothFragment fragment = weakFragment.get();
        if (fragment != null) {
            host.start(fragment.getContext());
            fragment.setInitializing();
            fragment.disableInteraction();
        }
    }

    public void joinBluetooth() {
        guest = new BleGuest(new BleCallback() {
            @Override
            public void bleSuccess(Bitmap bitmap) {
                guest.stop();
                callback.bleRetrieveSuccess(bitmap);
            }

            @Override
            public void bleFailed(Exception exception) {
                guest.stop();
                BluetoothFragment fragment = weakFragment.get();
                if (fragment != null) {
                    fragment.displayToast(exception.toString());
                    fragment.enableInteraction();
                }
            }

            @Override
            public void bleProgress(int progress) {
                BluetoothFragment fragment = weakFragment.get();
                if (fragment != null) {
                    fragment.setProgress(progress);
                }
            }
        }, callback.getBitmap());

        BluetoothFragment fragment = weakFragment.get();
        if (fragment != null) {
            guest.start(fragment.getContext());
            fragment.setInitializing();
            fragment.disableInteraction();
        }
    }

    public void cancelClicked() {
        BluetoothFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.popFragment();
        }
    }

    public void viewGone() {
        if (host != null) {
            host.stop();
        }

        if (guest != null) {
            guest.stop();
        }
    }

    public void blePermissionsDenied() {
        BluetoothFragment fragment = weakFragment.get();
        if (fragment != null) {
            fragment.popFragment();
        }
    }

}
