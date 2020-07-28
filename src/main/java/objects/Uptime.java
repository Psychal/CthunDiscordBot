package objects;

public class Uptime {
    private static long start;

    public static void setStart(long start) {
        Uptime.start = start;
        System.out.println(start);
    }

    public static String getUptime(long end){
        long difference = Math.abs(end - start);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = difference / daysInMilli;
        difference = difference % daysInMilli;
        long elapsedHours = difference / hoursInMilli;
        difference = difference % hoursInMilli;
        long elapsedMinutes = difference / minutesInMilli;
        difference = difference % minutesInMilli;
        long elapsedSeconds = difference / secondsInMilli;

        String uptime;
        if(elapsedDays == 0 && elapsedHours != 0) {
            uptime = String.format("%d "+(elapsedHours == 1 ? "hour" : "hours")+", %d "+(elapsedMinutes == 1 ? "minute" : "minutes")+", %d seconds%n",
                    elapsedHours, elapsedMinutes, elapsedSeconds);
        }
        else if(elapsedHours == 0){
            uptime = String.format("%d "+(elapsedMinutes == 1 ? "minute" : "minutes")+", %d seconds%n",
                    elapsedMinutes, elapsedSeconds);
        }
        else{
            uptime = String.format("%d "+(elapsedDays == 1 ? "day" : "days")+", %d "+(elapsedHours == 1 ? "hour" : "hours")+", %d "+(elapsedMinutes == 1 ? "minute" : "minutes")+", %d seconds%n",
                    elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
        }
        return uptime;
    }
}
