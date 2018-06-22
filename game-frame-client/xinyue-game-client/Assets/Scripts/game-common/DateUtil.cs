using System;

public class DateUtil  {

    /// <summary>  
    /// 获取当前本地时间戳  
    /// </summary>  
    /// <returns></returns>        
    public static long GetCurrentTimeUnix()
    {
        TimeSpan cha = (DateTime.Now - TimeZone.CurrentTimeZone.ToLocalTime(new System.DateTime(1970, 1, 1)));
        long t = (long)cha.TotalMilliseconds;
        return t;
    }
    /// <summary>  
    /// 时间戳转换为本地时间对象  
    /// </summary>  
    /// <returns></returns>        
    public static DateTime GetLocalUnixDateTime(long unix)
    {
        //long unix = 1500863191;  
        DateTime dtStart = TimeZone.CurrentTimeZone.ToLocalTime(new DateTime(1970, 1, 1));
        DateTime newTime = dtStart.AddSeconds(unix);
        return newTime;
    }
}
