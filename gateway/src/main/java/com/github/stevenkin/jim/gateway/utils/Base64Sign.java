package com.github.stevenkin.jim.gateway.utils;

import java.io.UnsupportedEncodingException;

public class Base64Sign {
    static final int CHUNK_SIZE = 76;
    static final byte[] CHUNK_SEPARATOR = "\r\n".getBytes();
    static final int BASELENGTH = 255;
    static final int LOOKUPLENGTH = 64;
    static final int EIGHTBIT = 8;
    static final int SIXTEENBIT = 16;
    static final int TWENTYFOURBITGROUP = 24;
    static final int FOURBYTE = 4;
    static final int SIGN = -128;
    static final byte PAD = 61;
    private static byte[] base64Alphabet = new byte[255];
    private static byte[] lookUpBase64Alphabet = new byte[64];

    public Base64Sign() {
    }

    private static boolean isBase64(byte octect) {
        if (octect == 61) {
            return true;
        } else {
            return base64Alphabet[octect] != -1;
        }
    }

    public static boolean isArrayByteBase64(byte[] arrayOctect) {
        arrayOctect = discardWhitespace(arrayOctect);
        int length = arrayOctect.length;
        if (length == 0) {
            return true;
        } else {
            for(int i = 0; i < length; ++i) {
                if (!isBase64(arrayOctect[i])) {
                    return false;
                }
            }

            return true;
        }
    }

    public static byte[] encodeBase64(byte[] binaryData) {
        return encodeBase64(binaryData, false);
    }

    public static byte[] encodeBase64Chunked(byte[] binaryData) {
        return encodeBase64(binaryData, true);
    }

    public static byte[] decode(byte[] pArray) {
        return decodeBase64(pArray);
    }

    public static byte[] encodeBase64(byte[] binaryData, boolean isChunked) {
        int lengthDataBits = binaryData.length * 8;
        int fewerThan24bits = lengthDataBits % 24;
        int numberTriplets = lengthDataBits / 24;
        int encodedDataLength = 0;
        int nbrChunks = 0;
        if (fewerThan24bits != 0) {
            encodedDataLength = (numberTriplets + 1) * 4;
        } else {
            encodedDataLength = numberTriplets * 4;
        }

        if (isChunked) {
            nbrChunks = CHUNK_SEPARATOR.length == 0 ? 0 : (int)Math.ceil((double)((float)encodedDataLength / 76.0F));
            encodedDataLength += nbrChunks * CHUNK_SEPARATOR.length;
        }

        byte[] encodedData = new byte[encodedDataLength];
        int encodedIndex = 0;
        int nextSeparatorIndex = 76;
        int chunksSoFar = 0;

        byte val1;
        byte val2;
        byte k;
        byte l;
        byte b1;
        byte b2;
        int dataIndex;
        int i;
        for(i = 0; i < numberTriplets; ++i) {
            dataIndex = i * 3;
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            byte b3 = binaryData[dataIndex + 2];
            l = (byte)(b2 & 15);
            k = (byte)(b1 & 3);
            val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
            val2 = (b2 & -128) == 0 ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 240);
            byte val3 = (b3 & -128) == 0 ? (byte)(b3 >> 6) : (byte)(b3 >> 6 ^ 252);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | k << 4];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2 | val3];
            encodedData[encodedIndex + 3] = lookUpBase64Alphabet[b3 & 63];
            encodedIndex += 4;
            if (isChunked && encodedIndex == nextSeparatorIndex) {
                System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedIndex, CHUNK_SEPARATOR.length);
                ++chunksSoFar;
                nextSeparatorIndex = 76 * (chunksSoFar + 1) + chunksSoFar * CHUNK_SEPARATOR.length;
                encodedIndex += CHUNK_SEPARATOR.length;
            }
        }

        dataIndex = i * 3;
        if (fewerThan24bits == 8) {
            b1 = binaryData[dataIndex];
            k = (byte)(b1 & 3);
            val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex + 2] = 61;
            encodedData[encodedIndex + 3] = 61;
        } else if (fewerThan24bits == 16) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte)(b2 & 15);
            k = (byte)(b1 & 3);
            val1 = (b1 & -128) == 0 ? (byte)(b1 >> 2) : (byte)(b1 >> 2 ^ 192);
            val2 = (b2 & -128) == 0 ? (byte)(b2 >> 4) : (byte)(b2 >> 4 ^ 240);
            encodedData[encodedIndex] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex + 1] = lookUpBase64Alphabet[val2 | k << 4];
            encodedData[encodedIndex + 2] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex + 3] = 61;
        }

        if (isChunked && chunksSoFar < nbrChunks) {
            System.arraycopy(CHUNK_SEPARATOR, 0, encodedData, encodedDataLength - CHUNK_SEPARATOR.length, CHUNK_SEPARATOR.length);
        }

        return encodedData;
    }

    public static byte[] decodeBase64(byte[] base64Data) {
        base64Data = discardNonBase64(base64Data);
        if (base64Data.length == 0) {
            return new byte[0];
        } else {
            int numberQuadruple = base64Data.length / 4;
            int encodedIndex = 0;
            int i = base64Data.length;

            while(base64Data[i - 1] == 61) {
                --i;
                if (i == 0) {
                    return new byte[0];
                }
            }

            byte[] decodedData = new byte[i - numberQuadruple];

            for(i = 0; i < numberQuadruple; ++i) {
                int dataIndex = i * 4;
                byte marker0 = base64Data[dataIndex + 2];
                byte marker1 = base64Data[dataIndex + 3];
                byte b1 = base64Alphabet[base64Data[dataIndex]];
                byte b2 = base64Alphabet[base64Data[dataIndex + 1]];
                byte b3;
                if (marker0 != 61 && marker1 != 61) {
                    b3 = base64Alphabet[marker0];
                    byte b4 = base64Alphabet[marker1];
                    decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
                    decodedData[encodedIndex + 1] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                    decodedData[encodedIndex + 2] = (byte)(b3 << 6 | b4);
                } else if (marker0 == 61) {
                    decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
                } else if (marker1 == 61) {
                    b3 = base64Alphabet[marker0];
                    decodedData[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
                    decodedData[encodedIndex + 1] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                }

                encodedIndex += 3;
            }

            return decodedData;
        }
    }

    static byte[] discardWhitespace(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;
        int i = 0;

        while(i < data.length) {
            switch(data[i]) {
                default:
                    groomedData[bytesCopied++] = data[i];
                case 9:
                case 10:
                case 13:
                case 32:
                    ++i;
            }
        }

        byte[] packedData = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }

    static byte[] discardNonBase64(byte[] data) {
        byte[] groomedData = new byte[data.length];
        int bytesCopied = 0;

        for(int i = 0; i < data.length; ++i) {
            if (isBase64(data[i])) {
                groomedData[bytesCopied++] = data[i];
            }
        }

        byte[] packedData = new byte[bytesCopied];
        System.arraycopy(groomedData, 0, packedData, 0, bytesCopied);
        return packedData;
    }

    public static byte[] encode(byte[] pArray) {
        return encodeBase64(pArray, false);
    }

    public static String encode(String str) throws UnsupportedEncodingException {
        String baseStr = new String(encode(str.getBytes("UTF-8")));
        String tempStr = Digest.digest(str).toUpperCase();
        String result = tempStr + baseStr;
        return new String(encode(result.getBytes("UTF-8")));
    }

    public static String decode(String cryptoStr) throws UnsupportedEncodingException {
        if (cryptoStr.length() < 40) {
            return "";
        } else {
            try {
                String tempStr = new String(decode(cryptoStr.getBytes("UTF-8")));
                String result = tempStr.substring(40, tempStr.length());
                return new String(decode(result.getBytes("UTF-8")));
            } catch (ArrayIndexOutOfBoundsException var3) {
                return "";
            }
        }
    }

    public static byte[] decode2(String encoded) {
        if (encoded == null) {
            return null;
        } else {
            char[] base64Data = encoded.toCharArray();
            int len = removeWhiteSpace(base64Data);
            if (len % 4 != 0) {
                return null;
            } else {
                int numberQuadruple = len / 4;
                if (numberQuadruple == 0) {
                    return new byte[0];
                } else {
                    byte[] decodedData = null;
                    int i = 0;
                    int encodedIndex = 0;
                    int dataIndex = 0;

                    byte b1;
                    byte b2;
                    byte b3;
                    byte b4;
                    char d1;
                    char d2;
                    char d3;
                    char d4;
                    for(decodedData = new byte[numberQuadruple * 3]; i < numberQuadruple - 1; ++i) {
                        if (!isData(d1 = base64Data[dataIndex++]) || !isData(d2 = base64Data[dataIndex++]) || !isData(d3 = base64Data[dataIndex++]) || !isData(d4 = base64Data[dataIndex++])) {
                            return null;
                        }

                        b1 = base64Alphabet[d1];
                        b2 = base64Alphabet[d2];
                        b3 = base64Alphabet[d3];
                        b4 = base64Alphabet[d4];
                        decodedData[encodedIndex++] = (byte)(b1 << 2 | b2 >> 4);
                        decodedData[encodedIndex++] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                        decodedData[encodedIndex++] = (byte)(b3 << 6 | b4);
                    }

                    if (isData(d1 = base64Data[dataIndex++]) && isData(d2 = base64Data[dataIndex++])) {
                        b1 = base64Alphabet[d1];
                        b2 = base64Alphabet[d2];
                        d3 = base64Data[dataIndex++];
                        d4 = base64Data[dataIndex++];
                        if (isData(d3) && isData(d4)) {
                            b3 = base64Alphabet[d3];
                            b4 = base64Alphabet[d4];
                            decodedData[encodedIndex++] = (byte)(b1 << 2 | b2 >> 4);
                            decodedData[encodedIndex++] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                            decodedData[encodedIndex++] = (byte)(b3 << 6 | b4);
                            return decodedData;
                        } else {
                            byte[] tmp;
                            if (isPad(d3) && isPad(d4)) {
                                if ((b2 & 15) != 0) {
                                    return null;
                                } else {
                                    tmp = new byte[i * 3 + 1];
                                    System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                                    tmp[encodedIndex] = (byte)(b1 << 2 | b2 >> 4);
                                    return tmp;
                                }
                            } else if (!isPad(d3) && isPad(d4)) {
                                b3 = base64Alphabet[d3];
                                if ((b3 & 3) != 0) {
                                    return null;
                                } else {
                                    tmp = new byte[i * 3 + 2];
                                    System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                                    tmp[encodedIndex++] = (byte)(b1 << 2 | b2 >> 4);
                                    tmp[encodedIndex] = (byte)((b2 & 15) << 4 | b3 >> 2 & 15);
                                    return tmp;
                                }
                            } else {
                                return null;
                            }
                        }
                    } else {
                        return null;
                    }
                }
            }
        }
    }

    private static boolean isWhiteSpace(char octect) {
        return octect == ' ' || octect == '\r' || octect == '\n' || octect == '\t';
    }

    private static boolean isData(char octect) {
        return octect < 255 && base64Alphabet[octect] != -1;
    }

    private static boolean isPad(char octect) {
        return octect == '=';
    }

    private static int removeWhiteSpace(char[] data) {
        if (data == null) {
            return 0;
        } else {
            int newSize = 0;
            int len = data.length;

            for(int i = 0; i < len; ++i) {
                if (!isWhiteSpace(data[i])) {
                    data[newSize++] = data[i];
                }
            }

            return newSize;
        }
    }

    static {
        int i;
        for(i = 0; i < 255; ++i) {
            base64Alphabet[i] = -1;
        }

        for(i = 90; i >= 65; --i) {
            base64Alphabet[i] = (byte)(i - 65);
        }

        for(i = 122; i >= 97; --i) {
            base64Alphabet[i] = (byte)(i - 97 + 26);
        }

        for(i = 57; i >= 48; --i) {
            base64Alphabet[i] = (byte)(i - 48 + 52);
        }

        base64Alphabet[43] = 62;
        base64Alphabet[47] = 63;

        for(i = 0; i <= 25; ++i) {
            lookUpBase64Alphabet[i] = (byte)(65 + i);
        }

        i = 26;

        int j;
        for(j = 0; i <= 51; ++j) {
            lookUpBase64Alphabet[i] = (byte)(97 + j);
            ++i;
        }

        i = 52;

        for(j = 0; i <= 61; ++j) {
            lookUpBase64Alphabet[i] = (byte)(48 + j);
            ++i;
        }

        lookUpBase64Alphabet[62] = 43;
        lookUpBase64Alphabet[63] = 47;
    }
}

