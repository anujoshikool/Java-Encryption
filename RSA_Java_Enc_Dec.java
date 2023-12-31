import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
//import java.util.Scanner;
//import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
public class RSAUtil {

    //private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
    //private static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAUZV+tjiNBKhlBZbKBnzeugpdYPhh5PbHanjV0aQ+LF7vetPYhbTiCVqA3a+Chmge44+prlqd3qQCYra6OYIe7oPVq4mETa1c/7IuSlKJgxC5wMqYKxYydb1eULkrs5IvvtNddx+9O/JlyM5sTPosgFHOzr4WqkVtQ71IkR+HrAgMBAAECgYAkQLo8kteP0GAyXAcmCAkA2Tql/8wASuTX9ITD4lsws/VqDKO64hMUKyBnJGX/91kkypCDNF5oCsdxZSJgV8owViYWZPnbvEcNqLtqgs7nj1UHuX9S5yYIPGN/mHL6OJJ7sosOd6rqdpg6JRRkAKUV+tmN/7Gh0+GFXM+ug6mgwQJBAO9/+CWpCAVoGxCA+YsTMb82fTOmGYMkZOAfQsvIV2v6DC8eJrSa+c0yCOTa3tirlCkhBfB08f8U2iEPS+Gu3bECQQCrG7O0gYmFL2RX1O+37ovyyHTbst4s4xbLW4jLzbSoimL235lCdIC+fllEEP96wPAiqo6dzmdH8KsGmVozsVRbAkB0ME8AZjp/9Pt8TDXD5LHzo8mlruUdnCBcIo5TMoRG2+3hRe1dHPonNCjgbdZCoyqjsWOiPfnQ2Brigvs7J4xhAkBGRiZUKC92x7QKbqXVgN9xYuq7oIanIM0nz/wq190uq0dh5Qtow7hshC/dSK3kmIEHe8z++tpoLWvQVgM538apAkBoSNfaTkDZhFavuiVl6L8cWCoDcJBItip8wKQhXwHp0O3HLg10OEd14M58ooNfpgt+8D8/8/2OOFaR0HzA+2Dm";

    public static PublicKey getPublicKey(String base64PublicKey){
        PublicKey publicKey = null;
        try{
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64PublicKey.getBytes()));
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey getPrivateKey(String base64PrivateKey){
        PrivateKey privateKey = null;
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64PrivateKey.getBytes()));
        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public static byte[] encrypt(String data, String publicKey) throws BadPaddingException, IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    public static String decrypt(byte[] data, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidKeySpecException {
        try {
        	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return new String(cipher.doFinal(data));
        }
    	catch (Exception e) {
    		System.out.println("Error");
    		throw new InvalidKeySpecException("Inavalid Key");
    	}
    }

    public static String decrypt(String data, String base64PrivateKey) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException {
        return decrypt(Base64.getDecoder().decode(data.getBytes(StandardCharsets.UTF_8)), getPrivateKey(base64PrivateKey));
    }

    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, NoSuchPaddingException, BadPaddingException, IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        if (args.length == 4) {	
			String str = args[1];
		if ("-enc".equals(args[0])) {
			if (args[1].length() > 0) {
		    
				String fileName = args[3];
		    if ("-pub".equals(args[2])) {
		    	if (fileName.length() > 0) {
		    	try {
		        String publicKey1 = new String(Files.readAllBytes(Paths.get(fileName)));
		        //System.out.println("PUBLIC:\n" + publicKey1);
		        String publicKey = publicKey1.replace("-----BEGIN OF PUBLIC KEY-----","");
		        publicKey = publicKey.replace("-----END OF PUBLIC KEY-----","");
		        publicKey=publicKey.replaceAll("\\s+","");
		        //System.out.println("PUBLIC:\n" + publicKey);
		    	String encryptedString = Base64.getEncoder().encodeToString(encrypt(str, publicKey));
		    	System.out.println("SUCCESS:\n" + encryptedString);
		    	 
		    
		    		}
		    		catch (Exception e) {
		        		System.out.println("Error:PLEASE PROVIDE THE EXACT PATH OF FILE OR PROVIDE RESPECTIVE KEY AS 'PUBLIC KEY' FOR ENCRYPTION AND 'PRIVATE KEY' FOR DECRYPTION:");
		        		throw new  java.nio.file.FileSystemException("Inavalid Path or Provide Respective Key");
		    		}
		    	}
		    	else {
		    		System.out.println("kuch toh dede");
		    	}
		    	
		    }
		    else {
		    	System.out.println("-pub bhej");
		    }
		    
        	}
			else {
				System.out.println("string > 0");
			}
			
     	}
     	   else if("-dec".equals(args[0]))
     	   {
    		   String encryptedString = args[1];
	        //String encryptedString = "K44jpspi7BND0lBOraXlP4vJGbfjZOpbOvr8A+MkFDfdcEo8GAVsrLob07puau+x/JY24Go4z2ZDuxvpv1H+AxkFre2Wb3lUtlk9Q4pybNDqUUjQ+5OKvfsYEn9ENFquKIL1bSWx8sdbdFGLVaCeeDubOedMWY5LFkcJ8kOScp4=";
               if (args[1].length() > 0) {
               if ("-priv".equals(args[2])) {
		String fileName = args[3];
		if (fileName.length() > 0) {
		  try {
		  String privateKey1 = new String(Files.readAllBytes(Paths.get(fileName)));
		  //System.out.println("PRIV:\n" + privateKey1);
		  String privateKey = privateKey1.replace("-----BEGIN OF PRIVATE KEY-----","");
		   privateKey = privateKey.replace("-----END OF PRIVATE KEY-----","");
		//System.out.println("PRIV:\n" + privateKey);
		   privateKey=privateKey.replaceAll("\\s+","");
    	  String decryptedString = RSAUtil.decrypt(encryptedString, privateKey);
		  System.out.println(decryptedString);
		  }
		
			catch (Exception e) {
				System.out.println("Error:PLEASE PROVIDE THE EXACT PATH OF FILE OR PROVIDE RESPECTIVE KEY AS 'PUBLIC KEY' FOR ENCRYPTION AND 'PRIVATE KEY' FOR DECRYPTION11:");
				throw new  java.nio.file.FileSystemException("Inavalid Path or Provide Respective Key");
			}
		}
		
		  
               }
               else {
            	   System.out.println("Do pass -priv for dec");
               }
               
               
            }  
     }
		
     	   else
     	   {
    		   System.out.println("PLEASE DO PASS -(enc/dec)");
     	   }
    	       
	} 	
		else {
			System.out.println("PLEASE PROVIDE ALL THE FOUR ARGUMENTS:");
		}

    }
}
