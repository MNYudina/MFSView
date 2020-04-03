package io.github.mnyudina.motifs.util;
/**
 * 
 * @author Gleepa
 * 
 *
 */
public class FormatUtils {
	
    /**
     * Formats duration in nanoseconds in one of the following formats:
     * <ul>
     * <li> #ms          - if duration is less than 1 second
     * <li> #sec #ms     - if duration is less than 1 minute
     * <li> #min #sec    - if duration is less than 1 hour
     * <li> #h #min #sec - otherwise
     * </ul>
     *
     * @author Andrey Kurchanov, Gleepa
     * @param durationNanos duration
     * @return formatted duration
     */
    public static String durationToHMS(long durationNanos) {
        long durationMills = durationNanos / 1000000;
        if (durationMills < 1000) {
            return durationMills + " ms";
        }
        long durationSec = durationMills / 1000;
        if (durationSec < 60) {
            return String.format("%d sec %d ms", durationSec, durationMills % 1000);
        }
        if (durationSec < 60 * 60) {
            return String.format("%d min %d sec", durationSec / 60, durationSec % 60);
        }
        return String.format("%d h %d min %d sec", durationSec / 3600, (durationSec % 3600) / 60, durationSec % 60);
    }
    
}