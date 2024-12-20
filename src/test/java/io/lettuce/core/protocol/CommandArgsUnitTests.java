package io.lettuce.core.protocol;

import static io.lettuce.TestTags.UNIT_TEST;
import static org.assertj.core.api.Assertions.*;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.StringCodec;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Unit tests for {@link CommandArgs}.
 *
 * @author Mark Paluch
 */
@Tag(UNIT_TEST)
class CommandArgsUnitTests {

    @Test
    void getFirstIntegerShouldReturnNull() {

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).add("foo");

        assertThat(CommandArgsAccessor.getFirstInteger(args)).isNull();
    }

    @Test
    void getFirstIntegerShouldReturnFirstInteger() {

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).add(1L).add(127).add(128).add(129).add(0)
                .add(-1);

        assertThat(CommandArgsAccessor.getFirstInteger(args)).isEqualTo(1L);
    }

    @Test
    void getFirstIntegerShouldReturnFirstNegativeInteger() {

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).add(-1L).add(-127).add(-128).add(-129);

        assertThat(CommandArgsAccessor.getFirstInteger(args)).isEqualTo(-1L);
    }

    @Test
    void getFirstIntegerShouldReturnFirstPositiveLong() {

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).add(Long.MAX_VALUE);

        assertThat(CommandArgsAccessor.getFirstInteger(args)).isEqualTo(Long.MAX_VALUE);
    }

    @Test
    void getFirstIntegerShouldReturnFirstNegativeLong() {

        assertThat(CommandArgsAccessor.getFirstInteger(new CommandArgs<>(StringCodec.UTF8).add(Long.MIN_VALUE)))
                .isEqualTo(Long.MIN_VALUE);
        assertThat(CommandArgsAccessor.getFirstInteger(new CommandArgs<>(StringCodec.UTF8).add(Long.MIN_VALUE + 2)))
                .isEqualTo(Long.MIN_VALUE + 2);
        assertThat(CommandArgsAccessor.getFirstInteger(new CommandArgs<>(StringCodec.UTF8).add(Integer.MIN_VALUE)))
                .isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    void getFirstStringShouldReturnNull() {

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).add(1);

        assertThat(CommandArgsAccessor.getFirstString(args)).isNull();
    }

    @Test
    void getFirstStringShouldReturnFirstString() {

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).add("one").add("two");

        assertThat(CommandArgsAccessor.getFirstString(args)).isEqualTo("one");
    }

    @Test
    void getFirstCharArrayShouldReturnCharArray() {

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).add(1L).add("two".toCharArray());

        assertThat(CommandArgsAccessor.getFirstCharArray(args)).isEqualTo("two".toCharArray());
    }

    @Test
    void getFirstCharArrayShouldReturnNull() {

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).add(1L);

        assertThat(CommandArgsAccessor.getFirstCharArray(args)).isNull();
    }

    @Test
    void getFirstEncodedKeyShouldReturnNull() {

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).add(1L);

        assertThat(CommandArgsAccessor.getFirstString(args)).isNull();
    }

    @Test
    void getFirstEncodedKeyShouldReturnFirstKey() {

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).addKey("one").addKey("two");

        assertThat(CommandArgsAccessor.encodeFirstKey(args)).isEqualTo(ByteBuffer.wrap("one".getBytes()));
    }

    @Test
    void addValues() {

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).addValues(Arrays.asList("1", "2"));

        ByteBuf buffer = Unpooled.buffer();
        args.encode(buffer);

        ByteBuf expected = Unpooled.buffer();
        expected.writeBytes(("$1\r\n" + "1\r\n" + "$1\r\n" + "2\r\n").getBytes());

        assertThat(buffer.toString(StandardCharsets.US_ASCII)).isEqualTo(expected.toString(StandardCharsets.US_ASCII));
    }

    @Test
    void addByte() {

        CommandArgs<String, String> args = new CommandArgs<>(StringCodec.UTF8).add("one".getBytes());

        ByteBuf buffer = Unpooled.buffer();
        args.encode(buffer);

        ByteBuf expected = Unpooled.buffer();
        expected.writeBytes(("$3\r\n" + "one\r\n").getBytes());

        assertThat(buffer.toString(StandardCharsets.US_ASCII)).isEqualTo(expected.toString(StandardCharsets.US_ASCII));
    }

    @Test
    void addByteUsingByteCodec() {

        CommandArgs<byte[], byte[]> args = new CommandArgs<>(ByteArrayCodec.INSTANCE).add("one".getBytes());

        ByteBuf buffer = Unpooled.buffer();
        args.encode(buffer);

        ByteBuf expected = Unpooled.buffer();
        expected.writeBytes(("$3\r\n" + "one\r\n").getBytes());

        assertThat(buffer.toString(StandardCharsets.US_ASCII)).isEqualTo(expected.toString(StandardCharsets.US_ASCII));
    }

    @Test
    void addValueUsingByteCodec() {

        CommandArgs<byte[], byte[]> args = new CommandArgs<>(ByteArrayCodec.INSTANCE).addValue("one".getBytes());

        ByteBuf buffer = Unpooled.buffer();
        args.encode(buffer);

        ByteBuf expected = Unpooled.buffer();
        expected.writeBytes(("$3\r\n" + "one\r\n").getBytes());

        assertThat(buffer.toString(StandardCharsets.US_ASCII)).isEqualTo(expected.toString(StandardCharsets.US_ASCII));
    }

    @Test
    void addKeyUsingByteCodec() {

        CommandArgs<byte[], byte[]> args = new CommandArgs<>(ByteArrayCodec.INSTANCE).addValue("one".getBytes());

        ByteBuf buffer = Unpooled.buffer();
        args.encode(buffer);

        ByteBuf expected = Unpooled.buffer();
        expected.writeBytes(("$3\r\n" + "one\r\n").getBytes());

        assertThat(buffer.toString(StandardCharsets.US_ASCII)).isEqualTo(expected.toString(StandardCharsets.US_ASCII));
    }

}
