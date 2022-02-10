package it.unipr.advmobdev.mat301275.facemorph.blemanager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.ParcelUuid;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import it.unipr.advmobdev.mat301275.facemorph.opencv.Utilities;

public class BleGuest {
    private BleCallback callback;
    private Context context;

    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;

    private BluetoothLeScanner mBluetoothLeScanner;

    private BluetoothGattCharacteristic charTx;
    private BluetoothGattCharacteristic charRx;

    private BluetoothDevice connectedDevice;

    private byte[] bytesToSend;

    private int currentMtu = 0;

    private BleGuest.State state = BleGuest.State.IDLE;
    enum State {
        IDLE,
        RECEIVING,
        SENDING,
        STOPPED,
        COMPLETED,
    }

    public BleGuest(BleCallback callback, Bitmap bitmap) {
        this.callback = callback;
        this.bytesToSend = Utilities.bitmapToByteArray(bitmap);
    }


    public void start(Context context) {
        this.state = State.IDLE;
        this.context = context;

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

        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
        ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        ScanFilter scanFilter = new ScanFilter.Builder().setServiceUuid(new ParcelUuid(BleService.serviceUUID)).build();

        ArrayList<ScanFilter> scanFilters = new ArrayList<>();
        scanFilters.add(scanFilter);

        mBluetoothLeScanner.startScan(scanFilters, scanSettings, scanCallback);

    }

    public void stop() {
        if (mBluetoothLeScanner != null) {
            mBluetoothLeScanner.stopScan(scanCallback);
        }
    }

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            BleGuest.this.connectedDevice = result.getDevice();
            result.getDevice().connectGatt(BleGuest.this.context, false, bluetoothGattCallback);
            mBluetoothLeScanner.stopScan(scanCallback);
            super.onScanResult(callbackType, result);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            callback.bleFailed(new Exception("BLE scan failed"));
        }
    };

    BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyUpdate(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
            super.onPhyRead(gatt, txPhy, rxPhy, status);
        }

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothGatt.STATE_CONNECTED) {
                    BleGuest.this.state = State.RECEIVING;
                    gatt.discoverServices();
                } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
                    if (BleGuest.this.state == State.RECEIVING || BleGuest.this.state == State.SENDING) {
                        callback.bleFailed(new Exception("BLE Device disconnected"));
                    }
                }
            } else {
                gatt.close();
                callback.bleFailed(new Exception("BLE connection failed"));
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            BluetoothGattService service = gatt.getService(BleService.serviceUUID);

            charTx = service.getCharacteristic(BleService.characteristicTxUUID);
            charRx = service.getCharacteristic(BleService.characteristicRxUUID);

            gatt.requestMtu(500);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.i("BleNick", "Notifica dato");
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            Log.d("BleNick", "Scritto descrittore");
        }

        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            Log.d("mtu", new String(String.valueOf(mtu)));
            BleGuest.this.currentMtu = mtu;

            gatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);

            gatt.setCharacteristicNotification(charTx, true);
            UUID notificationsUUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
            BluetoothGattDescriptor notificationDescriptor = charTx.getDescriptor(notificationsUUID);
            notificationDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(notificationDescriptor);
        }
    };

}
