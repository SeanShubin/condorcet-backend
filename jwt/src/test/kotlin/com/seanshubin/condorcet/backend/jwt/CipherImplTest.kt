package com.seanshubin.condorcet.backend.jwt

import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.exceptions.TokenExpiredException
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormat
import com.seanshubin.condorcet.backend.string.util.ByteArrayFormatServiceLocator
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CipherImplTest {
    @Test
    fun roundTrip() {
        val tester = createTester()
        val original = mapOf("foo" to "bar")
        val encoded = tester.cipher.encode(original, ClockStub.minutes(1))
        val decoded = tester.cipher.decode(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun notExpiredYet() {
        val tester = createTester()
        val original = mapOf("foo" to "bar")
        val encoded = tester.cipher.encode(original, ClockStub.minutes(2))
        tester.clock.passTime(ClockStub.minutes(1))
        val decoded = tester.cipher.decode(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun expired() {
        val tester = createTester()
        val original = mapOf("foo" to "bar")
        val encoded = tester.cipher.encode(original, ClockStub.minutes(2))
        tester.clock.passTime(ClockStub.minutes(3))
        assertFailsWith<TokenExpiredException> {
            tester.cipher.decode(encoded)
        }
    }

    @Test
    fun willNotDecodeUsingNewKeys() {
        val oldTester = createTester(oldPublicKey, oldPrivateKey)
        val newTester = createTester(newPublicKey, newPrivateKey)
        val original = mapOf("foo" to "bar")
        val encoded = oldTester.cipher.encode(original, ClockStub.minutes(1))
        assertFailsWith<SignatureVerificationException> {
            newTester.cipher.decode(encoded)
        }
    }

    private fun createTester(): Tester = createTester(oldPublicKey, oldPrivateKey)

    private fun createTester(publicKeyHex: String, privateKeyHex: String): Tester =
        Tester(publicKeyHex, privateKeyHex)

    class Tester(publicKeyHex: String, privateKeyHex: String) {
        val byteArrayFormat = ByteArrayFormatServiceLocator.byteArrayFormat
        val keyStore = KeyStoreStub(byteArrayFormat, publicKeyHex, privateKeyHex)
        val algorithmFactory = AlgorithmFactoryImpl(keyStore)
        val clock = ClockStub()
        val cipher = CipherImpl(algorithmFactory, clock)
    }

    class KeyStoreStub(private val byteArrayFormat:ByteArrayFormat,
                       private val publicKeyEncoded: String,
                       private val privateKeyEncoded: String) : KeyStore {
        override fun privateKey(): ByteArray {
            return byteArrayFormat.decode(privateKeyEncoded)
        }

        override fun publicKey(): ByteArray {
            return byteArrayFormat.decode(publicKeyEncoded)
        }
    }

    companion object {
        val oldPrivateKey =
            """30 82 02 76 02 01 00 30 0D 06 09 2A 86 48 86 F7
0D 01 01 01 05 00 04 82 02 60 30 82 02 5C 02 01
00 02 81 81 00 AC 6F 83 1D 5A 8D D2 88 D2 C9 9A
E1 8A E5 49 F3 89 9A A3 78 C1 8D 68 67 F6 DC 2A
C9 1B B4 10 E7 B3 BE 32 9E D9 BE C3 C7 DE 0C 75
6E 21 AF 91 C4 A4 95 7E 9D CA D6 04 1E C3 CE 6B
16 54 55 BC 3D EF 5E D7 96 46 B1 37 57 5B 3F C7
51 50 BF 48 C4 C3 4D ED 91 34 8D 1E 3C DF 92 5E
67 3C D4 D5 15 39 07 8E CD C3 F8 80 12 FB 3F BD
62 F9 E5 57 00 D8 72 C4 44 27 F3 7E 4F 54 F0 FE
DA 10 04 78 F9 02 03 01 00 01 02 81 80 2E 06 05
64 CC 4A 96 2A D6 B7 E0 92 DA 53 A0 8D 53 E8 E6
40 D0 C8 41 59 9E 55 DD 45 98 13 53 7F C4 45 BB
95 12 61 0C 38 48 57 E2 EF E6 25 CA 2E 46 0F 70
A0 05 07 62 4A 23 D0 F0 5C 21 5E 50 C3 76 FB FE
03 17 90 D6 8B 4D 28 45 23 93 23 EE 08 2A 3F 73
3B 5D 56 86 7F CC E6 C9 62 21 94 48 20 D1 C1 38
24 36 71 65 B8 35 3F 8B 8B 00 20 AE 1F 8E E8 0C
85 38 48 60 BA B1 A7 EE 47 07 C4 2C D1 02 41 00
D1 20 44 A1 EA C3 88 3A 3D DE 3F D0 06 3C C5 A0
A9 2E 7E 67 D1 93 2F A5 F1 49 F7 28 4B 26 02 D2
8C 68 8B E0 67 F3 1E DA B1 AB BD 39 88 82 4B 03
8E 09 DA 79 D6 A3 3A 11 99 CF E2 15 E5 0D 1C AB
02 41 00 D3 15 EE 95 AC D5 4C D2 3E C1 71 C9 E0
BB BF 8D D2 B5 56 10 9D 8C 58 74 CF 64 D2 20 DE
81 31 5F 9F E3 4D 13 E6 72 70 79 C0 5C 8C F9 71
AF 2D 00 C9 BE 0A 52 10 04 52 F2 D2 72 E5 2A E0
A1 78 EB 02 40 3E 73 47 6B 9F 02 E3 44 99 43 2C
85 3F C7 98 30 E1 34 EC 34 AE A6 28 9D 82 8F D7
0A 26 5E E4 8A E1 B9 3A C4 39 E6 A5 79 23 83 A8
CF 6C 8F 94 3D 2C 86 8E C2 C1 B1 1E DF 30 B2 9E
CE 71 07 58 5F 02 41 00 B9 00 CC E5 B3 E0 12 7F
66 6F 26 EA B7 A3 F5 A4 38 5B A4 3D D5 C7 6E B3
9A 1D 94 29 28 BA 74 6A 89 F8 AE D2 55 34 51 AD
F3 D1 69 E1 2A 29 4D 39 16 98 0F F5 92 82 27 AD
18 93 88 95 06 E1 9E D7 02 40 27 38 0B F1 91 8C
56 68 8B 02 E7 03 A1 0D 74 06 2C B6 BE 6A F6 5F
54 73 27 0A 60 6A 6B 5A E8 66 73 00 30 FB F1 A5
7D F8 7F 6A BB 92 88 DD 53 58 A2 61 FE 4E 2E 21
55 2C D3 31 55 56 C9 D6 6C 55"""

        val oldPublicKey = """30 81 9F 30 0D 06 09 2A 86 48 86 F7 0D 01 01 01
05 00 03 81 8D 00 30 81 89 02 81 81 00 AC 6F 83
1D 5A 8D D2 88 D2 C9 9A E1 8A E5 49 F3 89 9A A3
78 C1 8D 68 67 F6 DC 2A C9 1B B4 10 E7 B3 BE 32
9E D9 BE C3 C7 DE 0C 75 6E 21 AF 91 C4 A4 95 7E
9D CA D6 04 1E C3 CE 6B 16 54 55 BC 3D EF 5E D7
96 46 B1 37 57 5B 3F C7 51 50 BF 48 C4 C3 4D ED
91 34 8D 1E 3C DF 92 5E 67 3C D4 D5 15 39 07 8E
CD C3 F8 80 12 FB 3F BD 62 F9 E5 57 00 D8 72 C4
44 27 F3 7E 4F 54 F0 FE DA 10 04 78 F9 02 03 01
00 01"""

        val newPrivateKey = """30 82 02 77 02 01 00 30 0D 06 09 2A 86 48 86 F7
0D 01 01 01 05 00 04 82 02 61 30 82 02 5D 02 01
00 02 81 81 00 C2 9D DC 1F 55 68 00 24 34 63 BA
F5 83 8E 75 D7 BE 12 F2 10 04 D3 0D 8D 88 75 D8
FB 98 6B 58 3D D5 1D 3B 7C 49 E3 D7 1A 99 62 48
32 26 44 CC 28 DB E1 D6 CB 4F 3E 9D 2E 33 38 4C
34 7F 22 2E 62 60 60 30 38 EA 0D 62 1C D0 6D C4
C6 34 B0 EE 30 F6 5B C7 FB D3 1A 77 00 D4 70 E0
89 68 F4 ED 09 71 CB 19 FD 32 98 F7 72 CB B2 F4
91 64 96 F7 93 AB 37 FA 60 B1 97 13 80 D6 BB 14
A3 5F 77 1C 9D 02 03 01 00 01 02 81 80 03 38 D3
D5 1C F7 DC D0 BC A1 4E 0E 14 80 7E 48 F5 17 A1
34 4C B0 25 D0 4B 0B 10 6D 91 E7 6D 0D 91 02 AB
E2 71 E2 B8 C3 F2 F8 22 93 D9 F8 57 9C F1 8E FA
9C 61 12 7B 4D B1 81 64 19 96 65 3D 79 52 06 56
2C 24 05 48 EB 55 48 07 15 37 EB 3E F1 5B D2 AD
06 34 22 DC 96 C6 AD 20 42 7E 01 F8 4F AC 03 3D
EB C9 BA 11 26 72 77 4D B5 96 9F 47 CF 30 6C 26
93 76 43 36 37 19 7D 41 B4 AF 9C 88 21 02 41 00
FC E5 F2 0E BB 0E 3D C8 B6 28 35 5E 8D D0 E8 17
67 D1 CC 84 4E 05 73 70 49 F3 20 69 A3 7E D6 E8
50 32 16 81 DB FB 06 03 B1 D8 FF F7 0F C9 DC 98
2E 50 C2 46 00 CA 51 78 D4 D7 7B 20 68 06 6C 49
02 41 00 C5 00 EB B5 1C 05 4A 67 DF E8 83 13 E4
1C 75 52 B5 EF 00 AF A3 08 1A 10 98 AB C6 59 47
5B 95 DC 28 B2 46 7B A8 49 BD 67 A4 2C 7E A7 F0
15 70 48 96 4E 17 66 3B 23 F1 DA C6 95 38 50 DF
16 25 B5 02 40 36 63 05 31 61 CC E8 40 97 DC 01
CE 39 D5 69 74 4F 21 71 48 2A B7 45 62 2A A5 1C
9D 5B 49 D5 D1 D4 1D 51 75 CF E7 26 1D F9 4B 5F
90 B4 56 87 18 3B 2D A6 FA 03 05 7F 8B EA CA 04
72 AD AC 63 F1 02 41 00 8F D8 7D 43 4F C3 79 31
E1 A7 57 D6 75 50 66 46 4F 7B C5 47 79 C6 57 A9
D8 A0 4C 83 31 FC 68 AF 49 B3 EA 8D 2E 33 31 AE
74 8C DC AA 69 3E 6E 2F 42 38 A1 13 4F 89 8E 76
4D 5B F5 B3 EF 40 AF A5 02 41 00 83 81 24 B2 F8
30 85 36 B5 8B 22 FB E1 22 01 07 57 BE F7 55 45
1E D3 D5 30 76 6E 76 7D B5 78 F3 C3 A6 9C 55 6B
81 95 AF C8 01 1F D5 4E 32 60 40 71 6E CE 76 DC
F4 B8 52 C2 F4 2D 02 5D 62 18 5F"""

        val newPublicKey = """30 81 9F 30 0D 06 09 2A 86 48 86 F7 0D 01 01 01
05 00 03 81 8D 00 30 81 89 02 81 81 00 C2 9D DC
1F 55 68 00 24 34 63 BA F5 83 8E 75 D7 BE 12 F2
10 04 D3 0D 8D 88 75 D8 FB 98 6B 58 3D D5 1D 3B
7C 49 E3 D7 1A 99 62 48 32 26 44 CC 28 DB E1 D6
CB 4F 3E 9D 2E 33 38 4C 34 7F 22 2E 62 60 60 30
38 EA 0D 62 1C D0 6D C4 C6 34 B0 EE 30 F6 5B C7
FB D3 1A 77 00 D4 70 E0 89 68 F4 ED 09 71 CB 19
FD 32 98 F7 72 CB B2 F4 91 64 96 F7 93 AB 37 FA
60 B1 97 13 80 D6 BB 14 A3 5F 77 1C 9D 02 03 01
00 01"""
    }
}