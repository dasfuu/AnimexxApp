package de.meisterfuu.animexx.xmpp;

import java.io.FileDescriptor;

import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public class XMPPBinder implements IBinder {

	@Override
	public void dump(FileDescriptor fd, String[] args) throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void dumpAsync(FileDescriptor fd, String[] args)
			throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getInterfaceDescriptor() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isBinderAlive() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void linkToDeath(DeathRecipient recipient, int flags)
			throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean pingBinder() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IInterface queryLocalInterface(String descriptor) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean transact(int code, Parcel data, Parcel reply, int flags)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
		// TODO Auto-generated method stub
		return false;
	}

}
