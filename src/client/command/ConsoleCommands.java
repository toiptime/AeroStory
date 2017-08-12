/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.command;

import client.MapleCharacter;
import net.server.Server;
import net.server.channel.Channel;
import net.server.world.World;
import tools.MaplePacketCreator;

/**
 *
 * @author David
 * @credit: Hades for (Examples)
 * @credit: kevintjuh93 (connect the string in the array)
 */

public class ConsoleCommands {
    
    public static boolean executeCommand(String[] sub, char heading) {
        Server srv = Server.getInstance();
        World worldServer = null;
        
        switch (sub[0].toLowerCase()) {
            case "gmonline":
                for (Channel ch : srv.getAllChannels()) {
                    String s = "Personajes en Linea (Mundo: " + ch.getWorld() + " Canal: " + ch.getId() + " En Linea: " + ch.getPlayerStorage().getAllCharacters().size() + ") : ";
                    if (ch.getPlayerStorage().getAllCharacters().size() < 50) {
                        for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters()) {
                            if (chr.isGM()) {
                                s += MapleCharacter.makeMapleReadable(chr.getName()) + ", ";
                            }
                        }
                        System.out.println(s.substring(0, s.length() - 2));
                    }
                }
                break;
            case "online":
                for (Channel ch : srv.getAllChannels()) {
                        String s = "Personajes en Linea (Canal " + ch.getId() + " En Linea: " + ch.getPlayerStorage().getAllCharacters().size() + ") : ";
                        if (ch.getPlayerStorage().getAllCharacters().size() < 50) {
                            for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters()) {
                                s += MapleCharacter.makeMapleReadable(chr.getName()) + ", ";
                            }
                            System.out.println(s.substring(0, s.length() - 2));
                        }
                    }
                break;
            case "shutdown":
                srv.shutdown();
                break;
            case "restart":
                srv.restart();
                break;
            case "exprate":
                if (sub.length <= 1) {
                    worldServer = srv.getWorld(Integer.parseInt(sub[1]));
                    worldServer.setExpRate(Integer.parseInt(sub[2]));
                    for (MapleCharacter mc : worldServer.getPlayerStorage().getAllCharacters()) {
                        mc.setRates();
                    }
                    break;
                } else {
                    System.out.println("[Comando] !exprate {world #} {rate}");
                }
            case "saveall":
                try {
                    for (World world : Server.getInstance().getWorlds()) {
                        for (MapleCharacter chr : world.getPlayerStorage().getAllCharacters()) {
                            chr.saveToDB();
                        }
                    }
                    System.out.println("[Noticia] World Saved!");
                } catch (Exception e) {
                    System.out.println("[Noticia] Error has occur!");
                }
                break;
            case "servermessage":
                worldServer = srv.getWorld(Integer.parseInt(sub[1]));
                worldServer.setServerMessage(joinStringFrom(sub, 2));
                break;
            case "notice":
            case "message":
                try {
                    srv.broadcastMessage(Integer.parseInt(sub[1]), MaplePacketCreator.serverNotice(6, " " + joinStringFrom(sub, 2)));
                    System.out.println("[Noticia] " + joinStringFrom(sub, 2));
                } catch (Exception ex) {
                    System.out.println("Por favor, introduzca el mundo correcto (a partir de 0)");
                }
                break;
            default:
                return false;
        }
        return true;
    }
    
    private static String joinStringFrom(String arr[], int start) {
        return joinStringFrom(arr, start, arr.length - 1);
    }

    private static String joinStringFrom(String arr[], int start, int end) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < arr.length; i++) {
            builder.append(arr[i]);
            if (i != end) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }
}
