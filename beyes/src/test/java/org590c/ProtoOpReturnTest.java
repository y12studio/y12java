package org590c;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.primitives.Bytes;
import com.google.protobuf.InvalidProtocolBufferException;

import org590c.ProtoOpReturn.OpReturn;
import org590c.ProtoOpReturn.OpReturn.Tag;
import org590c.ProtoOpReturn.OpReturn.Type;
import tw.y12.beyes.Utils;

public class ProtoOpReturnTest {

	@Test
	public void testToString() throws InvalidProtocolBufferException {
		Tag tag = Tag.newBuilder().setTagInt32(1900).setTagString("hello 590c")
				.build();
		OpReturn opr = ProtoOpReturn.OpReturn.newBuilder().setType(Type.TAG)
				.setTag(tag).build();
		assertNotNull(opr);
		System.out.println(opr);
		assertEquals("0801120f0a0a68656c6c6f203539306310ec0e",
				Utils.HEX.encode(opr.toByteArray()));
		assertEquals("590c0801120f0a0a68656c6c6f203539306310ec0e",
				Utils.HEX.encode(Utils.wrap590c(opr.toByteArray())));

		OpReturn oprRestore = ProtoOpReturn.OpReturn.parseFrom(Utils.unwrap590c(Utils.HEX
				.decode("590c0801120f0a0a68656c6c6f203539306310ec0e")));
		assertNotNull(oprRestore);
		assertEquals(oprRestore.getType(), Type.TAG);
		assertEquals(oprRestore.getTag().getTagInt32(), 1900);
		assertEquals(oprRestore.getTag().getTagString(), "hello 590c");

	}

}
