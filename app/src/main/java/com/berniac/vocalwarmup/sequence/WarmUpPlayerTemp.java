package com.berniac.vocalwarmup.sequence;

/**
 * Created by Marina Gorlova on 02.12.2017.
 */
public class WarmUpPlayerTemp {/*implements Player {
    private long fullStepSizeInTicks;
    private long lowestUserTonicTicks;
    private long startingUserTonicTicks;
    private long highestUserTonicTicks;
    private long highestTonicTicks;
    private long highestUserTonicBackwardTicks;
    private long startingUserTonicBackwardTicks;
    private long lowestUserTonicBackwardTicks;
    private int lowestUserTonic;
    private int highestUserTonic;
    private long endTickPosition;
    private long directionStartPosition;
    private long directionEndPosition;

    private WarmUpSequenceTemp warmUpSequence;
    private Sequencer sequencer;
    private WarmUp warmUp;
    public long currentTickPosition;
    private long sequenceLengthInTicks;
    private int semitonesToLowerBoundary;
    private int semitonesToUpperBoundary;
    private int playFrom;

    int currentDirection = 0;

    public WarmUpPlayerTemp(WarmUpSequenceTemp warmUpSequence, final Sequencer sequencer, final WarmUp warmUp) {
        this.warmUpSequence = warmUpSequence;
        this.sequencer = sequencer;
        this.warmUp = warmUp;
        this.sequenceLengthInTicks = warmUpSequence.getSequence().getTickLength();

        int startingUserTonic = MidiUtils.getMidiNote(warmUp.getStartingNote());
        int lowestNoteInVoice = MidiUtils.getMidiNote(SequenceConstructor.getLowestNoteInVoice(
                warmUp.getMelody().getVoices().get(0)));
        int highestNoteInVoice = MidiUtils.getMidiNote(SequenceConstructor.getHighestNoteInVoice(
                warmUp.getMelody().getVoices().get(0)));
        int lowestUserTonic = SequenceConstructor.getLowestUserTonicInSequence(warmUp.getLowerNote(),
                lowestNoteInVoice, startingUserTonic, warmUp.getStep().getNextShift());
        int highestUserTonic = SequenceConstructor.getHighestUserTonicInSequence(warmUp.getUpperNote(),
                highestNoteInVoice, startingUserTonic, warmUp.getStep().getNextShift());

        int lowestTonic = warmUpSequence.getLowestTonic();
        int highestTonic = warmUpSequence.getHighestTonic();

        this.fullStepSizeInTicks = getFullStepSizeInTicks();

        // from lower to higher:
        this.lowestUserTonicTicks = getTonicTicks(lowestTonic, 0, lowestUserTonic);
        this.startingUserTonicTicks = getTonicTicks(lowestTonic, 0, startingUserTonic);
        this.highestUserTonicTicks = getTonicTicks(lowestTonic, 0, highestUserTonic);

        this.highestTonicTicks = getTonicTicks(lowestTonic, 0, highestTonic);

        // from higher to lower:
        this.highestUserTonicBackwardTicks = getTonicTicks(highestTonic, highestTonicTicks, highestUserTonic);
        this.startingUserTonicBackwardTicks = getTonicTicks(highestTonic, highestTonicTicks, startingUserTonic);
        this.lowestUserTonicBackwardTicks = getTonicTicks(highestTonic, highestTonicTicks, lowestUserTonic);

        System.out.println("Lowest: " + lowestTonic + " " + 0);
        System.out.println("User lowest: " + lowestUserTonic + " " + lowestUserTonicTicks);
        System.out.println("User starting: " + startingUserTonic + " " + startingUserTonicTicks);
        System.out.println("User highest: " + highestUserTonic + " " + highestUserTonicTicks);
        System.out.println("Highest: " + highestTonic + " " + highestTonicTicks);
        System.out.println("User highest: " + highestUserTonic + " " + highestUserTonicBackwardTicks);
        System.out.println("User starting: " + startingUserTonic + " " + startingUserTonicBackwardTicks);
        System.out.println("User lowest: " + lowestUserTonic + " " + lowestUserTonicBackwardTicks);

        currentDirection = 0;
        initDirectionBoundaries();


//        System.out.println("Lowest: " + lowestTonic + " " + 0);
//        System.out.println("User lowest: " + lowestUserTonic + " " + lowestUserTonicTicks);
//        System.out.println("User starting: " + startingUserTonic + " " + startingUserTonicTicks);
//        System.out.println("User highest: " + highestUserTonic + " " + highestUserTonicTicks);
//        System.out.println("Highest: " + highestTonic + " " + highestTonicTicks);
//        System.out.println("User highest: " + highestUserTonic + " " + highestUserTonicBackwardTicks);
//        System.out.println("User starting: " + startingUserTonic + " " + startingUserTonicBackwardTicks);
//        System.out.println("User lowest: " + lowestUserTonic + " " + lowestUserTonicBackwardTicks);


        try {
            sequencer.setSequence(warmUpSequence.getSequence());
            sequencer.open();
            sequencer.addEndSequenceEventListener(new EndSequenceEventListener() {
                @Override
                public void sequenceEnd() {
                    //todo
                    System.out.println("Listener: sequence finished");
                    if (currentDirection < warmUp.getDirections().length - 1) {
                        currentDirection++;
                        initDirectionBoundaries();
//                        sequencer.stop();
                        play();
                    }
                 }
            });
            // TODO close sequencer somewhere
        } catch (InvalidMidiDataException | MidiUnavailableException e) {
            System.out.println(e);
        }
    }

    void initDirectionBoundaries() {
        NoteValue lastMelodyNoteDuration =
                SequenceConstructor.getLastNoteDuration(warmUp.getMelody().getVoices().get(0));
        long lastMelodyNoteTicks = MidiUtils.getNoteValueInTicks(lastMelodyNoteDuration);

        Direction direction = warmUp.getDirections()[currentDirection];
        switch (direction) {
            case LOWER_TO_UPPER:
                directionStartPosition = lowestUserTonicTicks;
                directionEndPosition = highestUserTonicTicks;// - 1;
                break;
            case UPPER_TO_LOWER:
                directionStartPosition = highestUserTonicBackwardTicks;
                directionEndPosition = lowestUserTonicBackwardTicks;// - 1;
                break;
            case LOWER_TO_START:
                directionStartPosition = lowestUserTonicTicks;
                directionEndPosition = startingUserTonicTicks;// - 1;
                break;
            case START_TO_UPPER:
                directionStartPosition = startingUserTonicTicks;
                directionEndPosition = highestUserTonicTicks;// - 1;
                break;
            case UPPER_TO_START:
                directionStartPosition = highestUserTonicBackwardTicks;
                directionEndPosition = startingUserTonicBackwardTicks;// - 1;
                break;
            case START_TO_LOWER:
                directionStartPosition = startingUserTonicBackwardTicks;
                directionEndPosition = lowestUserTonicBackwardTicks; //- 1;
                break;
        }

        currentTickPosition = directionStartPosition;
        System.out.println("Playing from " + directionStartPosition + " to " + directionEndPosition);
    }


    long getTonicTicks(int tonicBefore, long tonicBeforeTicks, int tonic) {
        int stepsBetweenTonics = Math.abs(tonic - tonicBefore)/warmUp.getStep().getNextShift();
        return tonicBeforeTicks + stepsBetweenTonics * fullStepSizeInTicks;
    }

    @Override
    public void play() {
        System.out.println("Loop length " + sequencer.getTickLength());
        sequencer.setLoopEndPoint(directionEndPosition);
        sequencer.setLoopStartPoint(currentTickPosition);
        System.out.println("Set sequencer from " + directionEndPosition + " " + currentTickPosition);
        sequencer.start();
    }

    @Override
    public void pause() {
        sequencer.stop();
        try {
            sequencer.getReceiver().send(new ShortMessage(ShortMessage.CONTROL_CHANGE, 0x7B, 0), -1);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        currentTickPosition = sequencer.getTickPosition();
    }

    @Override
    public void stop() {
        sequencer.stop();
        try {
            sequencer.getReceiver().send(new ShortMessage(ShortMessage.CONTROL_CHANGE, 0x7B, 0), -1);
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }

        // TODO : Start all from beginning. No such button so far
        currentTickPosition = 0;
    }

    @Override
    public void openSettings() {

    }

    @Override
    public void openConstructor() {

    }

    private void goToStep(int stepNumber) {
        pause();
        currentTickPosition = stepNumber * getFullStepSizeInTicks();

        // Pressed previous step during the lowest step or next step during the highest step
        // in both cases go from the lowest note to the highest
        if (currentTickPosition >= sequenceLengthInTicks || currentTickPosition < 0) {
            currentTickPosition = lowestUserTonicTicks;
        }

//        // Start the new direction
//        if (currentTickPosition > directionEndPosition) {
//            currentTickPosition = directionEndPosition;
//        }
//        if (currentTickPosition < directionStartPosition) {
//            // TODO: start previous direction instead?
//            currentTickPosition = directionStartPosition;
//        }

        play();
    }

    // the lowest step is zero
    private int getCurrentStep() {
        return (int) Math.floor(1.0*sequencer.getTickPosition() / getFullStepSizeInTicks());
    }

    @Override
    public void repeatCurrentStep() {
        goToStep(getCurrentStep());
    }

    @Override
    public void previousStep() {
        goToStep(getCurrentStep() - 1);
    }

    @Override
    public void nextStep() {
        goToStep(getCurrentStep() + 1);
    }

    @Override
    public void melodyOn() {
        sequencer.setTrackMute(SequenceConstructor.MidiTrack.MELODY.getIndex(), false);

    }

    @Override
    public void melodyOff() {
        sequencer.setTrackMute(SequenceConstructor.MidiTrack.MELODY.getIndex(), true);
    }

    @Override
    public void harmonyOn() {
        sequencer.setTrackMute(SequenceConstructor.MidiTrack.HARMONY.getIndex(), false);
    }

    @Override
    public void harmonyOff() {
        sequencer.setTrackMute(SequenceConstructor.MidiTrack.HARMONY.getIndex(), true);
    }

    @Override
    public void recordOn() {

    }

    @Override
    public void recordOff() {

    }

    @Override
    public void changeTempo() {

    }

    @Override
    public void showNoteVisualisation() {

    }


    @Override
    public void showLyrics() {

    }

    @Override
    public Sequence getSequence() {
        return warmUpSequence.getSequence();
    }

    public static int getLengthInTicks(List<MusicalSymbol> musicalSymbols) {
        int length = 0;
        for (MusicalSymbol symbol: musicalSymbols) {
            length += MidiUtils.getNoteValueInTicks(symbol.getNoteValue());
        }
        return length;
    }

    int getFullStepSizeInTicks() {
        NoteValue lastMelodyNoteDuration =
                SequenceConstructor.getLastNoteDuration(warmUp.getMelody().getVoices().get(0));
        int pauseSize;
        if (warmUp.getAdjustmentRules() instanceof SilentAdjustmentRules) {
            pauseSize = warmUp.getPauseSize();
        } else {
            pauseSize = warmUp.getPauseSize() - (int)lastMelodyNoteDuration.getNumberOfQuarters();
        }
        return getLengthInTicks(warmUp.getMelody().getVoices().get(0).getMusicalSymbols())
                + pauseSize * MidiUtils.getNoteValueInTicks(NoteValue.QUARTER);
    }
*/}
