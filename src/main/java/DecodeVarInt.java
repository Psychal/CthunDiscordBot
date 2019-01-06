import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

public final class DecodeVarInt {

    public static int decodeVarInt(ByteBuffer src) throws BufferUnderflowException {
        int tmp;
        if ((tmp = src.get()) >= 0) {
            return tmp;
        }
        int result = tmp & 0x7f;
        if ((tmp = src.get()) >= 0) {
            result |= tmp << 7;
        } else {
            result |= (tmp & 0x7f) << 7;
            if ((tmp = src.get()) >= 0) {
                result |= tmp << 14;
            } else {
                result |= (tmp & 0x7f) << 14;
                if ((tmp = src.get()) >= 0) {
                    result |= tmp << 21;
                } else {
                    result |= (tmp & 0x7f) << 21;
                    result |= (tmp = src.get()) << 28;
                    while (tmp < 0) {
                        // We get into this loop only in the case of overflow.
                        tmp = src.get();
                    }
                }
            }
        }
        return result;
    }
}