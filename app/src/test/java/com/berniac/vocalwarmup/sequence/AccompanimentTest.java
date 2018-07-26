package com.berniac.vocalwarmup.sequence;


import org.junit.Test;

/**
 * Created by Mikhail Lipkovich on 2/25/2018.
 */
public class AccompanimentTest {
    @Test
    public void testParse() {
        Accompaniment accompaniment = Accompaniment.valueOf("Acc{\nVo{\n1=Fo[D-1,+1<Cis1,-1;D2,-2]\n2=Fo[D-1,+1<Hes,-1;Hes1,-2;E2,-3]\n3=Fo[F-1,+1<Aes,-1;Aes1,-2;E2,-3]\n}\nHa{\n1(2G-1,2G-1,2G-1)\n2(2E-1,2E-1,2E-1)\n3(2C-1,2H-2,2C-1)\n}\nMo(2){\nFull4<\nC[1(4G-1,4G-1,2G-1)2(4E-1,4D-1,2E-1)3(4C-1,4H-2,2C-1)]\nDes[1(4G-1,25N)2(4E-1,25N)3(4C-1,4Cis-1,4Dis-1,4Gis-2)]\nH[1(4G-1,25N)2(4E-1,25N)3(4C-1,8G-2,8Aes-2,4Hes-2,8H-2,8Fis-2)]\n>\n}\n}");
        System.out.println(accompaniment.getAdjustments());
    }
}
