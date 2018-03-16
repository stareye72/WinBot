/*
 * WINBOT
 */



import Prog1Tools.IOTools;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;
import org.jibble.pircbot.PircBot;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class WinBot
  extends PircBot
{
  static String hostname;
  static String name;
  static String channel;
  
  public static void main(String[] args)
    throws Exception
  {
    WinBot bot = new WinBot();
    name = IOTools.readString("Bitte geben Sie den Namen des Bots: ");
    hostname = IOTools.readString("Bitte geben Sie den Server ein: ");
    channel = IOTools.readString("Bitte geben Sie den channel ein: ");
    bot.getConnected(hostname, name, channel);
  }
  
  public void onMessage(String channel, String sender, String login, String hostname, String message)
  {
    WinBot bot = new WinBot();
    String url = "";
    sender="";
    if (message.matches("!time"))
    {
      String time = new Date().toString();
      sendMessage(channel, sender + ":Die Zeit ist jetzt " + time);
    }
    else if (message.matches("!ping"))
    {
      sendMessage(channel, sender + " :Pong");
    }
    else if (message.matches("!about"))
    {
      sendMessage(channel, sender + ":WinBot: Programmiert von Stareye, hilfe mit !hilfe");
    }
    else if (message.matches("!google"))
    {
      sendMessage(channel, sender + "https://www.google.de");
    }
    else if (message.matches("!hilfe"))
    {
      sendMessage(channel, sender + "Befehle: !time, !ping, !about, !hilfe, !google");
    }
    else if (message.matches("!test.*$"))
    {
      sendMessage(channel, sender + ":Dies ist ein Test");
    }
    else if (message.matches(".*(http|https)://.*$"))
    {
      Pattern p = Pattern.compile("(http|https)://.*$");
      Matcher m = p.matcher(message);
      while (m.find()) {
        url = m.group();
      }
      String title = bot.getTitle(url);
      sendMessage(channel, sender + " " + title);
    }
    else if (message.matches("!google.*$"))
    {
      String gsearch = "https://www.google.de/search?q=";
      String google = gsearch + message.substring(message.indexOf(" ") + 1).replaceAll(" ", "+");
      sendMessage(channel, sender + "" + google);
    }
  }
  
  public void getConnected(String hostname, String name, String channel)
    throws NickAlreadyInUseException, IrcException
  {
    WinBot bot = new WinBot();
    try
    {
      bot.setName(name);
      
      bot.setVerbose(true);
      
      bot.connect(hostname);
      
      bot.joinChannel(channel);
    }
    catch (IOException e)
    {
      System.err.println("An IOException was caught :" + e.getMessage());
    }
  }
  
  public String getTitle(String url)
  {
    String title = "";
    try
    {
      Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
      
      title = doc.title();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      return title;
    }
  }
  
  public void onDisconnect()
  {
    WinBot bot = new WinBot();
    try
    {
      bot.getConnected(hostname, name, channel);
    }
    catch (IrcException e)
    {
      e.printStackTrace();
    }
  }
}
