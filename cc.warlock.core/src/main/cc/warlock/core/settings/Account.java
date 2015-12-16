/**
 * Warlock, the open-source cross-platform game client
 *  
 * Copyright 2008, Warlock LLC, and individual contributors as indicated
 * by the @authors tag. 
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package cc.warlock.core.settings;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Collection;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;


public class Account extends WarlockSetting {

	private String accountName, password;
	private ProfileProvider profileProvider;
	
	public static String decryptPassword (byte[] encrypted)
	{
		if(encrypted == null)
			return null;
		DESEncrypter encrypter = new DESEncrypter("$warlockpassword$");
		return encrypter.decrypt(encrypted);
	}
	
	public static byte[] encryptPassword (String password)
	{
		if(password == null)
			return null;
		DESEncrypter encrypter = new DESEncrypter("$warlockpassword$");
		return encrypter.encrypt(password);
	}
	
	private static class DESEncrypter {
		private Cipher eCipher, dCipher;
		static final private byte[] salt = {
			(byte)0x98, (byte)0x93, (byte)0xA7, (byte)0x44,
			(byte)0x32, (byte)0x12, (byte)0x34, (byte)0xF3
		};
		static final private int iterations = 12;
		
		public DESEncrypter (String password)
		{
			try {
				KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, iterations);
				SecretKey key = SecretKeyFactory.getInstance(
					"PBEWithMD5AndDES").generateSecret(keySpec);
				eCipher = Cipher.getInstance(key.getAlgorithm());
				dCipher = Cipher.getInstance(key.getAlgorithm());
				
				AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterations);
				
				eCipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
				dCipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAlgorithmParameterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public byte[] encrypt (String text)
		{
			if (eCipher != null)
			{
				try {
					byte[] utf8 = text.getBytes("UTF8");
					byte[] encoded = eCipher.doFinal(utf8);
					return Base64.encodeBase64(encoded);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
			} else {
				return text.getBytes();
			}
		}
		
		public String decrypt (byte[] password)
		{
			if (dCipher != null)
			{
				try {
					byte[] encoded = Base64.decodeBase64(password);
					byte[] utf8 = dCipher.doFinal(encoded);
					
					return new String(utf8, "UTF8");
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			} else {
				return password.toString();
			}
		}
	}
	
	public Account (IWarlockSetting parent, String path) {
		super(parent, path);
		
		this.accountName = getNode().get("account-name", null);
		this.password = decryptPassword(getNode().getByteArray("password", null));
		profileProvider = new ProfileProvider(this);
		this.flush();
	}
	
	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		getNode().put("account-name", accountName);
		
		this.accountName = accountName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		getNode().putByteArray("password", encryptPassword(password));
		
		this.password = password;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof String)
		{
			return accountName.equals(obj);
		}
		
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return accountName.hashCode();
	}

	public ProfileProvider getProfileProvider() {
		return profileProvider;
	}
	
	public Collection<ProfileSetting> getProfiles() {
		return profileProvider.getSettings();
	}
}
