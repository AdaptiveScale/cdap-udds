package org.example.directives.utils;

import com.idealista.fpe.FormatPreservingEncryption;
import com.idealista.fpe.builder.FormatPreservingEncryptionBuilder;

public class FormatPreservingUtil {

    public static final byte[] KEY = new byte[]{
            (byte) 0xe0, (byte) 0x4f, (byte) 0xd0, (byte) 0x20,
            (byte) 0xea, (byte) 0x3a, (byte) 0x69, (byte) 0x10,
            (byte) 0xa2, (byte) 0xd8, (byte) 0x08, (byte) 0x00,
            (byte) 0x2b, (byte) 0x30, (byte) 0x30, (byte) 0x9d
    };

    public static final byte[] TWEAK = new byte[] {
            (byte) 0xEf5, (byte) 0x03, (byte) 0xF9
    };

    public static FormatPreservingEncryption defaultFormatPreservingEncryption(){
        return FormatPreservingEncryptionBuilder
                .ff1Implementation()
                .withDefaultDomain()
                .withDefaultPseudoRandomFunction(KEY)
                .withDefaultLengthRange()
                .build();
    }
}
