package it.unipr.advmobdev.mat301275.facemorph.blemanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattServer;
import android.bluetooth.BluetoothGattServerCallback;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.ParcelUuid;
import android.util.Log;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.common.primitives.Bytes;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import io.grpc.okhttp.internal.Util;
import it.unipr.advmobdev.mat301275.facemorph.opencv.Utilities;

public class BleHost {
    private BleCallback callback;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGattServer mBluetoothGattServer;
    private BluetoothLeAdvertiser mAdvertiser;
    private AdvertiseData mAdvData;
    private AdvertiseData mAdvScanResponse;
    private AdvertiseSettings mAdvSetting;

    private BluetoothGattCharacteristic charTx;
    private BluetoothGattCharacteristic charRx;

    private BluetoothDevice connectedDevice;

    private int currentProgress = -1;

    private int currentMtu = 0;

    private byte[] bytesToSend;
    private int index = 0;
    private int rcvSize = -1;
    private List<Byte> receivedBytes = new ArrayList<>();

    private State state = State.IDLE;
    enum State {
        IDLE,
        SENDING,
        RECEIVING,
        STOPPED
    }

    public BleHost(BleCallback callback, Bitmap bitmap) {
        this.callback = callback;
        this.bytesToSend = Utilities.bitmapToByteArray(bitmap);
    }

    public void start(Context context) {
        state = State.IDLE;

        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                callback.bleFailed(new Exception("Unable to start the Bluetooth Manager"));
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            callback.bleFailed(new Exception("Unable to start the Bluetooth Adapter"));
        }

        mBluetoothGattServer = mBluetoothManager.openGattServer(context, bluetoothGattServerCallback);

        BluetoothGattService service = new BluetoothGattService(BleService.serviceUUID, BluetoothGattService.SERVICE_TYPE_PRIMARY);

        charTx = new BluetoothGattCharacteristic(BleService.characteristicTxUUID,
                BluetoothGattCharacteristic.PROPERTY_READ | BluetoothGattCharacteristic.PROPERTY_NOTIFY,
                BluetoothGattCharacteristic.PERMISSION_READ);

        BluetoothGattDescriptor charTxDescriptor = new BluetoothGattDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"),
                BluetoothGattDescriptor.PERMISSION_READ | BluetoothGattDescriptor.PERMISSION_WRITE);

        charTxDescriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);

        charTx.addDescriptor(charTxDescriptor);

        charRx = new BluetoothGattCharacteristic(BleService.characteristicRxUUID,
                BluetoothGattCharacteristic.PROPERTY_WRITE | BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE,
                BluetoothGattCharacteristic.PERMISSION_WRITE);

        service.addCharacteristic(charTx);
        service.addCharacteristic(charRx);

        mAdvSetting = new AdvertiseSettings
                .Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setConnectable(true)
                .build();

        mAdvData = new AdvertiseData.Builder().setIncludeTxPowerLevel(false).addServiceUuid(new ParcelUuid(BleService.serviceUUID)).build();

        mAdvScanResponse = new AdvertiseData.Builder().setIncludeDeviceName(true).build();

        mAdvertiser = mBluetoothAdapter.getBluetoothLeAdvertiser();

        mAdvertiser.startAdvertising(mAdvSetting, mAdvData, advertiseCallback);

        mBluetoothGattServer.addService(service);
    }

    public void stop() {
        state = State.STOPPED;

        if (connectedDevice != null) {
            mBluetoothGattServer.cancelConnection(connectedDevice);
        }

        if (mAdvertiser != null) {
            mAdvertiser.stopAdvertising(advertiseCallback);
        }

        if (mBluetoothGattServer != null) {
            mBluetoothGattServer.close();
        }

    }

    private AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
        @Override
        public void onStartSuccess(AdvertiseSettings settingsInEffect) {
            super.onStartSuccess(settingsInEffect);
        }

        @Override
        public void onStartFailure(int errorCode) {
            super.onStartFailure(errorCode);
            callback.bleFailed(new Exception("Unable to start BLE advertising"));
        }
    };

    private BluetoothGattServerCallback bluetoothGattServerCallback = new BluetoothGattServerCallback() {
        @Override
        public void onConnectionStateChange(BluetoothDevice device, int status, int newState) {
            super.onConnectionStateChange(device, status, newState);
        }

        @Override
        public void onServiceAdded(int status, BluetoothGattService service) {
            super.onServiceAdded(status, service);
        }

        @Override
        public void onCharacteristicReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicReadRequest(device, requestId, offset, characteristic);
        }

        @Override
        public void onCharacteristicWriteRequest(BluetoothDevice device, int requestId, BluetoothGattCharacteristic characteristic, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onCharacteristicWriteRequest(device, requestId, characteristic, preparedWrite, responseNeeded, offset, value);
            if (rcvSize == -1) {
                ByteBuffer wrapped = ByteBuffer.wrap(value);
                rcvSize = wrapped.getInt();
                Log.i("BleNickSize", String.valueOf(BleHost.this.rcvSize));
            } else {
                receivedBytes.addAll(Bytes.asList(value));
                int newProgress = (int) ( (100.0 * receivedBytes.size())  / (2.0f * rcvSize)) + 50;
                if (currentProgress != newProgress) {
                    callback.bleProgress(newProgress);
                    currentProgress = newProgress;
                }
                Log.i("BleNickProgress",  String.valueOf(receivedBytes.size()));
                Log.i("BleNickMissing",  String.valueOf(rcvSize-receivedBytes.size()));
                if (receivedBytes.size() >= rcvSize) {
                    if (receivedBytes.size() > rcvSize) {
                        receivedBytes = receivedBytes.subList(0, rcvSize);
                    }
                    Log.i("BleNickProgressAdjust", String.valueOf(receivedBytes.size()));
                    byte[] bytes = Bytes.toArray(receivedBytes);
                    callback.bleSuccess(Utilities.byteArrayToBitmap(bytes));
                }
            }

        }

        @Override
        public void onDescriptorReadRequest(BluetoothDevice device, int requestId, int offset, BluetoothGattDescriptor descriptor) {
            super.onDescriptorReadRequest(device, requestId, offset, descriptor);
        }

        @Override
        public void onDescriptorWriteRequest(BluetoothDevice device, int requestId, BluetoothGattDescriptor descriptor, boolean preparedWrite, boolean responseNeeded, int offset, byte[] value) {
            super.onDescriptorWriteRequest(device, requestId, descriptor, preparedWrite, responseNeeded, offset, value);
            mBluetoothGattServer.sendResponse(device, requestId, BluetoothGatt.GATT_SUCCESS, offset, value);

            if (value[0] == 1) {
                BleHost.this.state = State.SENDING;

                byte[] messageLength = ByteBuffer.allocate(4).putInt(BleHost.this.bytesToSend.length).array();
                Log.i("BleNickSize", new String(String.valueOf(BleHost.this.bytesToSend.length)));
                index = 0;
                charTx.setValue(messageLength);
                mBluetoothGattServer.notifyCharacteristicChanged(device, charTx, false);

            } else if (value[0] == 0) {
                BleHost.this.state = State.RECEIVING;
            }
        }

        @Override
        public void onExecuteWrite(BluetoothDevice device, int requestId, boolean execute) {
            super.onExecuteWrite(device, requestId, execute);
        }

        @Override
        public void onNotificationSent(BluetoothDevice device, int status) {
            super.onNotificationSent(device, status);

            byte[] nextSlice = Arrays.copyOfRange(BleHost.this.bytesToSend, index * currentMtu, (index + 1) * currentMtu);
            int newProgress = (int) ( (100.0 * index * currentMtu)  / (2.0f * BleHost.this.bytesToSend.length));
            if (currentProgress != newProgress) {
                callback.bleProgress(newProgress);
                currentProgress = newProgress;
            }
            Log.i("BleNickIterationStart", String.valueOf(index * currentMtu));
            index += 1;
            charTx.setValue(nextSlice);
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mBluetoothGattServer.notifyCharacteristicChanged(device, charTx, false);
        }

        @Override
        public void onMtuChanged(BluetoothDevice device, int mtu) {
            super.onMtuChanged(device, mtu);
            BleHost.this.currentMtu = mtu - 3;
        }

        @Override
        public void onPhyUpdate(BluetoothDevice device, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(device, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyRead(BluetoothDevice device, int txPhy, int rxPhy, int status) {
            super.onPhyRead(device, txPhy, rxPhy, status);
        }
    };
}
