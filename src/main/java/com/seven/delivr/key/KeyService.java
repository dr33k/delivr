package com.seven.delivr.key;

import com.seven.delivr.base.AppService;
import com.seven.delivr.util.Utilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@Slf4j
@Service
public class KeyService implements AppService {
    private final Environment environment;

    public KeyService(Environment environment){
        this.environment = environment;
    }
    public byte[] getDecodedPrivateKeyBytes(){
        try {
            String rsaPrivateKey = environment.getProperty("ROOKS_API_DECRYPTION_KEY");
            return Base64.getDecoder().decode(rsaPrivateKey.getBytes(StandardCharsets.UTF_8));
        }catch (Exception ex){
          log.error(ex.getMessage());
          return null;
        }
    }
    public byte[] encrypt3DES(String payload){
        try {
            assert !Utilities.isEmpty(payload);

            final String THREE_DES_ALG = "DESede";
            final String THREE_DES_TRANS = "DESede/ECB/PKCS5Padding";

            //Get Key
            String flutterwaveEncKey = environment.getProperty("FLUTTERWAVE_ENCRYPTION_KEY");

            //Decode Key
            byte[] decodedKey = flutterwaveEncKey.getBytes(StandardCharsets.UTF_8);

            //Get SecretKeySpec object
            SecretKeySpec secretKeySpec = new SecretKeySpec(decodedKey, THREE_DES_ALG);

            //Initiate Cipher
            Cipher encrypt = Cipher.getInstance(THREE_DES_TRANS);
            encrypt.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            //Encrypt payload
            return Base64.getEncoder().encode(encrypt.doFinal(payload.getBytes(StandardCharsets.UTF_8)));

        }catch(Exception e){
            log.error(e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    public byte[] decryptRSA(String ciphertext){
        assert !Utilities.isEmpty(ciphertext);

        final String RSA_ALG = "RSA";
        final String RSA_TRANS = "RSA/ECB/PKCS1Padding";

        try {
            //Get Decoded Key
            byte[] privateKeyBytes = getDecodedPrivateKeyBytes();

            //Get PrivateKey Object
            PKCS8EncodedKeySpec pkSpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALG);
            PrivateKey privateKey = keyFactory.generatePrivate(pkSpec);

            //Initiate Cipher
            Cipher decrypt = Cipher.getInstance(RSA_TRANS);
            decrypt.init(Cipher.DECRYPT_MODE, privateKey);

            //Decrypt payload
            return decrypt.doFinal(Base64.getDecoder().decode(ciphertext.getBytes(StandardCharsets.UTF_8)));
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,  ex.getMessage());
        }

    }
}
