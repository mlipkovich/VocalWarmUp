package com.berniac.vocalwarmup.midi;

import com.berniac.vocalwarmup.sequence.Instrument;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by Mikhail Lipkovich on 2/18/2018.
 */
public class SF2Database {

    private static volatile Map<Instrument, Program> instrumentToProgram;

    private SF2Database() {}

    public static void configure(String str) {
        Map<Instrument, Program> localInstrumentToProgram = new HashMap<>();
        Scanner scanner = new Scanner(str);
        while (scanner.hasNext()) {
            String line = scanner.next();
            String[] programInstrument = line.split(":");
            Instrument instrument = Instrument.getByCode(programInstrument[0]);
            String[] programArr = programInstrument[1].split(",");
            Program program;
            if (programArr.length == 1) {
                program = new Program(Integer.valueOf(programArr[0]));
            } else {
                program = new Program(Integer.valueOf(programArr[0]), Integer.valueOf(programArr[1]));
            }
            localInstrumentToProgram.put(instrument, program);
        }
        scanner.close();
        synchronized (SF2Database.class) {
            instrumentToProgram = localInstrumentToProgram;
        }
    }

    public static Program getProgram(Instrument instrument) {
        return instrumentToProgram.get(instrument);
    }

    static class Program {
        private int bank;
        private int program;

        public Program(int bank, int program) {
            this.bank = bank;
            this.program = program;
        }

        public Program(int program) {
            this(0, program);
        }

        public int getBank() {
            return bank;
        }

        public int getProgram() {
            return program;
        }
    }
}
