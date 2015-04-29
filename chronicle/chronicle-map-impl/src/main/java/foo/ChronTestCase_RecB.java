package foo;

import foo.ChronTestCase.RecB;
import net.openhft.chronicle.hash.serialization.DeserializationFactoryConfigurableBytesReader;
import net.openhft.chronicle.hash.serialization.internal.ByteableMarshaller;
import net.openhft.lang.io.Bytes;
import net.openhft.lang.io.serialization.BytesMarshallable;
import net.openhft.lang.io.serialization.ObjectFactory;
import net.openhft.lang.model.Byteable;
import net.openhft.lang.model.Copyable;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static net.openhft.lang.Compare.calcLongHashCode;
import static net.openhft.lang.Compare.isEqual;

/**
 * A copy of the generated code
 */
public class ChronTestCase_RecB {

    public static class Native implements ChronTestCase.RecB, BytesMarshallable, Byteable, Copyable<foo.ChronTestCase.RecB> {
        private static final int INT = 0;
        private static final int STRING = 8;


        private Bytes _bytes;
        private long _offset;

        public void setIntAt(int i, int $) {
            if (i < 0) throw new ArrayIndexOutOfBoundsException(i + " must be greater than 0");
            if (i >= 2) throw new ArrayIndexOutOfBoundsException(i + " must be less than 2");
            _bytes.writeInt(_offset + INT + i * 4, $);
        }

        public int getIntAt(int i) {
            if (i < 0) throw new ArrayIndexOutOfBoundsException(i + " must be greater than 0");
            if (i >= 2) throw new ArrayIndexOutOfBoundsException(i + " must be less than 2");
            return _bytes.readInt(_offset + INT + i * 4);
        }

        public void setStringAt(int i, java.lang.String $) {
            if (i < 0) throw new ArrayIndexOutOfBoundsException(i + " must be greater than 0");
            if (i >= 2) throw new ArrayIndexOutOfBoundsException(i + " must be less than 2");
            _bytes.writeUTFΔ(_offset + STRING + i * 16, 16, $);
        }

        public java.lang.String getStringAt(int i) {
            if (i < 0) throw new ArrayIndexOutOfBoundsException(i + " must be greater than 0");
            if (i >= 2) throw new ArrayIndexOutOfBoundsException(i + " must be less than 2");
            return _bytes.readUTFΔ(_offset + STRING + i * 16);
        }

        @Override
        public void copyFrom(foo.ChronTestCase.RecB from) {
            for (int i = 0; i < 2; i++) {
                setIntAt(i, from.getIntAt(i));
            }
            for (int i = 0; i < 2; i++) {
                setStringAt(i, from.getStringAt(i));
            }
        }

        @Override
        public void writeMarshallable(Bytes out) {
            for (int i = 0; i < 2; i++) {
                out.writeInt(getIntAt(i));
            }
            for (int i = 0; i < 2; i++) {
                long pos = out.position();
                out.writeUTFΔ(getStringAt(i));
                long newPos = pos + 32;
                out.zeroOut(out.position(), newPos);
                out.position(newPos);
            }
        }

        @Override
        public void readMarshallable(Bytes in) {
            for (int i = 0; i < 2; i++) {
                setIntAt(i, in.readInt());
            }
            for (int i = 0; i < 2; i++) {
                setStringAt(i, in.readUTFΔ());
            }
        }

        @Override
        public void bytes(Bytes bytes, long offset) {
            this._bytes = bytes;
            this._offset = offset;
        }

        @Override
        public Bytes bytes() {
            return _bytes;
        }

        @Override
        public long offset() {
            return _offset;
        }

        @Override
        public int maxSize() {
            return 40;
        }

        public long longHashCode_int() {
            long hc = 0;
            for (int i = 0; i < 2; i++) {
                hc += calcLongHashCode(getIntAt(i));
            }
            return hc;
        }


        public long longHashCode_string() {
            long hc = 0;
            for (int i = 0; i < 2; i++) {
                hc += calcLongHashCode(getStringAt(i));
            }
            return hc;
        }

        public int hashCode() {
            long lhc = longHashCode();
            return (int) ((lhc >>> 32) ^ lhc);
        }

        public long longHashCode() {
            return (longHashCode_int()) * 10191 +
                    longHashCode_string();
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof ChronTestCase.RecB)) return false;
            ChronTestCase.RecB that = (ChronTestCase.RecB) o;

            for (int i = 0; i < 2; i++) {
                if (!isEqual(getIntAt(i), that.getIntAt(i))) return false;
            }
            for (int i = 0; i < 2; i++) {
                if (!isEqual(getStringAt(i), that.getStringAt(i))) return false;
            }
            return true;
        }

        public String toString() {
            if (_bytes == null) return "bytes is null";
            StringBuilder sb = new StringBuilder();
            sb.append("ChronTestCase.RecB{ ");
            sb.append("int").append("= [");
            for (int i = 0; i < 2; i++) {
                if (i > 0) sb.append(", ");
                sb.append(getIntAt(i));
            }
            sb.append("]");
            sb.append(", ")
            ;
            sb.append("string").append("= [");
            for (int i = 0; i < 2; i++) {
                if (i > 0) sb.append(", ");
                sb.append(getStringAt(i));
            }
            sb.append("]");
            sb.append(" }");
            return sb.toString();
        }
    }

    public static class BytesReader implements DeserializationFactoryConfigurableBytesReader<ChronTestCase.RecB, BytesReader> {
        private static final long serialVersionUID = 0L;

        public static final BytesReader INSTANCE = new BytesReader();

        BytesReader() {}

        @Override
        public BytesReader withDeserializationFactory(ObjectFactory<RecB> factory) {
            try {
                Class cfc = Class.forName("foo.foo.ChronTestCase$RecB$$BytesReader$WithCustomFactory");
                Constructor constructor = cfc.getConstructor(ObjectFactory.class);
                Object reader = constructor.newInstance(factory);
                return (BytesReader) reader;
            } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InstantiationException |
                    InvocationTargetException e) {
                throw new AssertionError(e);
            }
        }

        foo.ChronTestCase.RecB getInstance() throws Exception {
            return new Native();
        }

        @Override
        public RecB read(Bytes bytes, long size) {
            return read(bytes, size, null);
        }

        @Override
        public RecB read(Bytes bytes, long size, RecB toReuse) {
            try {
                if (toReuse == null)
                    toReuse = getInstance();
                if (toReuse instanceof Byteable) {
                    ByteableMarshaller.setBytesAndOffset(((Byteable) toReuse), bytes);
                    bytes.skip(size);
                    return toReuse;
                }
                for (int i = 0; i < 2; i++) {
                    toReuse.setIntAt(i, bytes.readInt());
                }
                for (int i = 0; i < 2; i++) {
                    long pos = bytes.position();
                    toReuse.setStringAt(i, bytes.readUTFΔ());
                    bytes.position(pos + 32);
                }
                return toReuse;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }

        public static class WithCustomFactory extends BytesReader {
            private static final long serialVersionUID = 0L;

            @NotNull
            private final ObjectFactory<RecB> factory;

            WithCustomFactory(@NotNull ObjectFactory<RecB> factory) {
                this.factory = factory;
            }

            @Override
            RecB getInstance() throws Exception {
                return factory.create();
            }
        }
    }

    public enum BytesWriter implements net.openhft.chronicle.hash.serialization.BytesWriter<RecB> {
        INSTANCE;

        @Override
        public long size(RecB e) {
            return 40;
        }

        @Override
        public void write(Bytes bytes, RecB e) {
            if (e instanceof Byteable) {
                Bytes eBytes = ((Byteable) e).bytes();
                if (eBytes != null) {
                    bytes.write(eBytes, ((Byteable) e).offset(), 40);
                } else {
                    throw new NullPointerException("You are trying to write a byteable object of " +
                            e.getClass() + ", " +
                            "which bytes are not assigned. I. e. most likely the object is uninitialized.");
                }
                return;
            }
            for (int i = 0; i < 2; i++) {
                bytes.writeInt(e.getIntAt(i));
            }
            for (int i = 0; i < 2; i++) {
                long pos = bytes.position();
                bytes.writeUTFΔ(e.getStringAt(i));
                long newPos = pos + 32;
                bytes.zeroOut(bytes.position(), newPos);
                bytes.position(newPos);
            }
        }

        private static void shouldNotBeNull() {
            throw new NullPointerException("Sub-members of RecB shouldn't be null for this writer. You should specify custom writer (e. g. using keyMarshallers()/valueMarshallers() methods ofChronicleMapBuilder, if you want to support null fields/array elements.");
        }
    }
}
