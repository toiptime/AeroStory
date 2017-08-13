/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as
 published by the Free Software Foundation version 3 as published by
 the Free Software Foundation. You may not use, modify or distribute
 this program under any other version of the GNU Affero General Public
 License.te

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package client.command;

import client.ChatLog;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import client.MapleDisease;
import client.MapleJob;
import client.MapleStat;
import client.Skill;
import client.SkillFactory;
import client.inventory.Equip;
import client.inventory.Item;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import constants.ItemConstants;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import net.server.Server;
import net.server.channel.Channel;
import net.server.world.World;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import scripting.npc.NPCScriptManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MaplePortal;
import server.MapleShopFactory;
import server.TimerManager;
import server.events.gm.MapleEvent;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleMonsterStats;
import server.life.MapleNPC;
import server.life.MobSkillFactory;
import server.maps.FieldLimit;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.DatabaseConnection;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.StringUtil;

public class Commands {

    public static boolean executePlayerCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter chr = c.getPlayer();
        if (heading == '!' && chr.gmLevel() == 0) {
            chr.yellowMessage("Usted no puede usar !" + sub + ", por favor, intentalo /" + sub);
            return false;
        }
        switch (sub[0]) {
                    case "comandos":
                    case "ayuda":
                    case "comando":
            chr.dropMessage("[AeroStory Comando de Jugadores]");
            chr.dropMessage("@ea - Si se le pega el NPC.");
            chr.dropMessage("@expfix - Si su experiencia le sale Negativo.");
            chr.dropMessage("@back - Para Darle su bienvenida.");
            chr.dropMessage("@goafk - Para estar en AFK");
            chr.dropMessage("@afk - Para estar los mapas de AFK");
            chr.dropMessage("@joinevent - Para entrar a los Eventos de los GMs");
        break;
        case "joinevent":
			if(!FieldLimit.CANNOTMIGRATE.check(chr.getMap().getFieldLimit())) {
				MapleEvent event = c.getChannelServer().getEvent();
				if(event != null) {
					if(event.getMapId() != chr.getMapId()) {
						if(event.getLimit() > 0) {
							chr.saveLocation("EVENT");

							if(event.getMapId() == 109080000 || event.getMapId() == 109060001)
								chr.setTeam(event.getLimit() % 2);

							event.minusLimit();

							chr.changeMap(event.getMapId());
						} else {
							chr.dropMessage("El limite de jugadores para el evento ya ha sido alcanzado.");
						}
					} else {
						chr.dropMessage(5, "Usted ya esta en el evento.");
					}
				} else {
					chr.dropMessage(5, "Actualmente no hay ningun evento en activo.");
				}
			} else {
				chr.dropMessage(5, "Actualmente se encuentra en un mapa en el que no puede unirse a un evento.");
			}
			break;
            case "ea":
                NPCScriptManager.getInstance().dispose(c);
                c.announce(MaplePacketCreator.enableActions());
                chr.message("Hecho, ya puede hablar con los NPC.");
                break;
            case "expfix":
            chr.setExp(0);
            chr.updateSingleStat(MapleStat.EXP, chr.getExp());
                break;
            case "back":
             c.getPlayer().resetAfkTime();
             chr.dropMessage("Bienvenido de regreso a AFK! :)");
             break;
            case "goafk":
                chr.setChalkboard("Estoy en AFK ~! Dejame un mensaje o susurro! =D");
		 break;
            case "afk":
                if (!chr.inJail()) {
                    chr.changeMap(209000008);
                    chr.getClient().getSession().write(MaplePacketCreator.getClock(36000)); // time in seconds
                    chr.dropMessage("Every 20 minutes, you will recieve 1 Sleepy Dream");
                    chr.dropMessage("Please Change Channel To Leave This Map");
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 1200000);
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 2400000);   
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 3600000);  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 4800000);  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 6000000);  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 7200000);  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 8400000);  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 9600000);  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 10800000);  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 12000000);     
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 13200000);                  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 14400000);  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 15600000);  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 16800000);  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 18000000);  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 19200000);  
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 20400000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 21600000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 22800000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 24000000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 25200000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 26400000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 27600000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 28800000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 30000000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 31200000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 32400000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 33600000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().saveToDB();
                    }
                }, 34800000); 
                TimerManager.getInstance().schedule(new Runnable() {
                    @Override
                    public void run() {
                        MapleInventoryManipulator.addById(c, 4001063, (short) 1);
                        c.getPlayer().changeMap(910000000);
                        c.getPlayer().saveToDB();
                    }
                }, 36000000);                                                               
                } else {
                    chr.dropMessage("You cannot use this command in this map");
                }  
                    break;
            case "rape":
                List<Pair<MapleBuffStat, Integer>> list = new ArrayList<>();
                list.add(new Pair<>(MapleBuffStat.MORPH, 8));
                list.add(new Pair<>(MapleBuffStat.CONFUSE, 1));
                chr.announce(MaplePacketCreator.giveBuff(0, 0, list));
                chr.getMap().broadcastMessage(chr, MaplePacketCreator.giveForeignBuff(chr.getId(), list));
                break;
            default:
                if (chr.gmLevel() == 0) {
                    chr.yellowMessage("Comando del Jugador " + heading + sub[0] + " no existe");
                }
                return false;
        }
        return true;
    }
    
    public static boolean executeDonatorCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter chr = c.getPlayer();
        if (heading == '!' && chr.gmLevel() == 0) {
            return false;
		}	      
        if (sub[0].equalsIgnoreCase("dcommands")) {
            chr.dropMessage("-----AeroStory Donador Comandos-----");
            chr.dropMessage("!buffme - Buff usted mismo");
            chr.dropMessage("!maxskills - Maximo de todas sus habilidades y obtener montajes frescos!");
            chr.dropMessage("!itemvac - Recoge todo los items");
         } else if (sub[0].equals("buffme")) {
            int[] array = {1001003, 2001002, 1101006, 1101007, 1301007, 2201001, 2121004, 2111005, 2311003, 1121002, 4211005, 3121002, 1121000, 2311003, 1101004, 1101006, 4101004, 4111001, 2111005, 1111002, 2321005, 3201002, 4101003, 4201002, 5101006, 1321010, 1121002, 1120003};
            for (int i = 0; i < array.length; i++) {
                SkillFactory.getSkill(array[i]).getEffect(SkillFactory.getSkill(array[i]).getMaxLevel()).applyTo(chr);
            }

        } else if (sub[0].equals("itemvac")) {
            List<MapleMapObject> items = chr.getMap().getMapObjectsInRange(chr.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.ITEM));
            for (MapleMapObject item : items) {
                MapleMapItem mapItem = (MapleMapItem) item;
                if (mapItem.getMeso() > 0) {
                    chr.gainMeso(mapItem.getMeso(), true);
                } else if (mapItem.getItem().getItemId() >= 5000000 && mapItem.getItem().getItemId() <= 5000100) {
                    int petId = MaplePet.createPet(mapItem.getItem().getItemId());
                    if (petId == -1) {
                    }
                    MapleInventoryManipulator.addById(c, mapItem.getItem().getItemId(), mapItem.getItem().getQuantity(), null, petId);
                } else {
                    MapleInventoryManipulator.addFromDrop(c, mapItem.getItem(), true);
                }
                mapItem.setPickedUp(true);
                chr.getMap().removeMapObject(item); // just incase ?
                chr.getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapItem.getObjectId(), 2, chr.getId()), mapItem.getPosition());
            }                 
            } else if (sub[0].equals("maxskills")) {
            for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img").getChildren()) {
                try {
                    Skill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                    chr.changeSkillLevel(skill, (byte) skill.getMaxLevel(), skill.getMaxLevel(), -1);
                } catch (NumberFormatException nfe) {
                    break;
                } catch (NullPointerException npe) {
                    continue;
                }

            }
        }
        return true;
    }

    public static boolean executeGMCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter player = c.getPlayer();
        Channel cserv = c.getChannelServer();
        Server srv = Server.getInstance();
        if (sub[0].equals("ap")) {
            player.setRemainingAp(Integer.parseInt(sub[1]));
        } else if (sub[0].equals("clockd")) {
            player.getMap().setClock(false);
            } else if (sub[0].equals("zakum")) {
            for (int m = 8800003; m <= 8800010; m++) {
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(m), player.getPosition());
            }
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8800000), player.getPosition());
            player.getMap().broadcastMessage(MaplePacketCreator.serverNotice(0, "El Todopoderoso Zakum ha despertado!"));
           } else if (sub[0].equals("mutemap")) {
            for (MapleCharacter chr : player.getMap().getCharacters())
                {
                if(chr.gmLevel()<=0)
                    chr.canTalk(!chr.getCanTalk());
                }
                    for (MapleCharacter chr : player.getMap().getCharacters())
                        chr.dropMessage("La capacidad de conversacion de este mapa se ha cambiado, por favor, escuche como un GM da instrucciones.");
                        player.dropMessage(6, "Hecho!");
        } else if (sub[0].equals("killnear")) {
            MapleMap map = player.getMap();
            List<MapleMapObject> players = map.getMapObjectsInRange(player.getPosition(), (double) 50000, Arrays.asList(MapleMapObjectType.PLAYER));
            for (MapleMapObject closeplayers : players) {
                MapleCharacter playernear = (MapleCharacter) closeplayers;
            if (playernear.isAlive() && playernear != player);
                playernear.setHp(0);
                playernear.updateSingleStat(MapleStat.HP, 0);
                playernear.dropMessage(6, "Estabas demasiado cerca de un GM.");
            }                    
        } else if (sub[0].equals("gmmap")) {
             player.changeMap(180000000);
        
         } else if (sub[0].equals("oxmap")) {
             player.changeMap(109020001);
        } else if (sub[0].equals("smegaoff")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            victim.setCanSmega(false);
            player.dropMessage("Has inhabilitado " + victim.getName() + "'s mPrivilegios del megafono");
            if (!(c.getPlayer().getName().equals(victim.getName()))) {
                player.dropMessage("Los privilegios del megafono han sido desactivados por una PISTOLA. Si continua con el spam, sera temp. Prohibido");
            }
          } else if (sub[0].equals("smegaon")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            victim.setCanSmega(true);
            player.dropMessage("You have enabled " + victim.getName() + "'s megaphone privilages");
            if (!(c.getPlayer().getName().equals(victim.getName()))) {
                player.dropMessage("Your megaphone privilages have been enabled by a GM. Please remember not to spam.");
            }                     
        } else if (sub[0].equals("tempban")) {
            Calendar tempB = Calendar.getInstance();
            String originalReason = StringUtil.joinAfterString(sub, ":");

            if (sub.length < 4 || originalReason == null) {
                player.dropMessage("Syntax helper: !tempban <name> [i / m / w / d / h] <amount> [r [reason id]] : Text Reason");
                //throw new IllegalCommandSyntaxException(4);
            }

            int yChange = StringUtil.getNamedIntArg(sub, 1, "y", 0);
            int mChange = StringUtil.getNamedIntArg(sub, 1, "m", 0);
            int wChange = StringUtil.getNamedIntArg(sub, 1, "w", 0);
            int dChange = StringUtil.getNamedIntArg(sub, 1, "d", 0);
            int hChange = StringUtil.getNamedIntArg(sub, 1, "h", 0);
            int iChange = StringUtil.getNamedIntArg(sub, 1, "i", 0);
            int gReason = StringUtil.getNamedIntArg(sub, 1, "r", 7);

            String reason = c.getPlayer().getName() + " tempbanned " + sub[1] + ": " + originalReason;

            if (gReason > 14) {
                player.dropMessage("Ha ingresado un ID de motivo de prohibicion incorrecto. Vuelva a intentarlo.");
                return true;
            }

            DateFormat df = DateFormat.getInstance();
            tempB.set(tempB.get(Calendar.YEAR) + yChange, tempB.get(Calendar.MONTH) + mChange, tempB.get(Calendar.DATE)
                    + (wChange * 7) + dChange, tempB.get(Calendar.HOUR_OF_DAY) + hChange, tempB.get(Calendar.MINUTE)
                    + iChange);

            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);

            if (victim == null) {
                int accId = MapleClient.findAccIdForCharacterName(sub[1]);
                if (accId >= 0 && MapleCharacter.tempban(reason, tempB, gReason, accId)) {
                    player.dropMessage("The character " + sub[1] + " has been successfully offline-tempbanned till "
                            + df.format(tempB.getTime()) + ".");
                } else {
                    player.dropMessage("There was a problem offline banning character " + sub[1] + ".");
                }
            } else {
                victim.tempban(reason, tempB, gReason);
                player.dropMessage("The character " + sub[1] + " has been successfully tempbanned till "
                        + df.format(tempB.getTime()));
            }
        }  else if (sub[0].equals("buffme")) {
            final int[] array = {9001000, 9101002, 9101003, 9101008, 2001002, 1101007, 1005, 2301003, 5121009, 1111002, 4111001, 4111002, 4211003, 4211005, 1321000, 2321004, 3121002};
            for (int i : array) {
                SkillFactory.getSkill(i).getEffect(SkillFactory.getSkill(i).getMaxLevel()).applyTo(player);
            }
        } else if (sub[0].equals("spawn")) {
            int mid = Integer.parseInt(sub[1]);
            int num = Math.min(StringUtil.getOptionalIntArg(sub, 2, 1), 500);
            Integer hp = StringUtil.getNamedIntArg(sub, 1, "hp");
            Integer exp = StringUtil.getNamedIntArg(sub, 1, "exp");
            Double php = StringUtil.getNamedDoubleArg(sub, 1, "php");
            Double pexp = StringUtil.getNamedDoubleArg(sub, 1, "pexp");
            MapleMonster onemob = MapleLifeFactory.getMonster(mid);
            int newhp = 0;
            int newexp = 0;
            double oldExpRatio = ((double) onemob.getHp() / onemob.getExp());
            if (hp != null) {
                newhp = hp.intValue();
            } else if (php != null) {
                newhp = (int) (onemob.getMaxHp() * (php.doubleValue() / 100));
            } else {
                newhp = onemob.getMaxHp();
            }
            if (exp != null) {
                newexp = exp.intValue();
            } else if (pexp != null) {
                newexp = (int) (onemob.getExp() * (pexp.doubleValue() / 100));
            } else {
                newexp = onemob.getExp();
            }

            if (newhp < 1) {
                newhp = 1;
            }

            MapleMonsterStats overrideStats = new MapleMonsterStats();
            overrideStats.setHp(newhp);
            overrideStats.setExp(newexp);
            overrideStats.setMp(onemob.getMaxMp());

            for (int i = 0; i < num; i++) {
                MapleMonster mob = MapleLifeFactory.getMonster(mid);
                mob.setHp(newhp);
                mob.setOverrideStats(overrideStats);
                c.getPlayer().getMap().spawnMonsterOnGroudBelow(mob, c.getPlayer().getPosition());

            }
            return true;
        } else if (sub[0].equals("bombmap")) {

            for (MapleCharacter chr : player.getMap().getCharacters()) {
            for (int i = 0; i < 250; i+=50) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300166), new Point(chr.getPosition().x - i, chr.getPosition().y));
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300166), new Point(chr.getPosition().x + i, chr.getPosition().y));
            }
            }
            player.dropMessage("Planted " + sub[1] + " bombs.");
            }
        else if (sub[0].equals("bomb")) {
                if (sub.length > 1) {
                    for (int i = 0; i < Integer.parseInt(sub[1]); i++) {
                        player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300166), player.getPosition());
                    }
            player.dropMessage("Planted " + sub[1] + " bombs.");
                } else {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(9300166), player.getPosition());
            player.dropMessage("Planted a bomb.");
            }
        }  else if (sub[0].equals("diseasemap")) {
            for (MapleCharacter victim : player.getMap().getCharacters()) {
            int type = 0;
            if (sub[2].equalsIgnoreCase("SEAL")) {
                type = 120;
            } else if (sub[2].equalsIgnoreCase("DARKNESS")) {
                type = 121;
            } else if (sub[2].equalsIgnoreCase("WEAKEN")) {
                type = 122;
            } else if (sub[2].equalsIgnoreCase("STUN")) {
                type = 123;
            } else if (sub[2].equalsIgnoreCase("POISON")) {
                type = 125;
            } else if (sub[2].equalsIgnoreCase("SEDUCE")) {
                type = 128;
            } else {
                player.dropMessage("ERROR.");
            }
            victim.giveDebuff(MapleDisease.getType(type), MobSkillFactory.getMobSkill(type, 1));
            } 
                    } else if (sub[0].equals("seduce")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            int level = Integer.parseInt(sub[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.SEDUCE, MobSkillFactory.getMobSkill(128, level));
            } else {
                player.dropMessage("Player is not on.");
            }
        } else if (sub[0].equals("stun")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            int level = Integer.parseInt(sub[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.STUN, MobSkillFactory.getMobSkill(123, level));
            } else {
                player.dropMessage("Player is not on.");
            }
        } else if (sub[0].equals("seal")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            int level = Integer.parseInt(sub[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.SEAL, MobSkillFactory.getMobSkill(120, level));
            } else {
                player.dropMessage("Player is not on.");
            }
         } else if (sub[0].equals("seducemap")) {
            for (MapleCharacter victim : player.getMap().getCharacters()) {
            int level = Integer.parseInt(sub[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.SEDUCE, MobSkillFactory.getMobSkill(128, level));
            } else {
                player.dropMessage("Player is not on.");
            }
            }
        } else if (sub[0].equals("stunmap")) {
            for (MapleCharacter victim : player.getMap().getCharacters()) {
            int level = Integer.parseInt(sub[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.STUN, MobSkillFactory.getMobSkill(123, level));
            } else {
                player.dropMessage("Player is not on.");
            }
            }
                                            } else if (sub[0].equals("healplayer")) {
                                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
                                    victim.setHp(victim.getCurrentMaxHp());
                                    victim.updateSingleStat(MapleStat.HP, victim.getHp());
                                    victim.setHp(victim.getCurrentMaxMp());
                                    victim.updateSingleStat(MapleStat.MP, victim.getMp());
        } else if (sub[0].equals("sealmap")) {
            for (MapleCharacter victim : player.getMap().getCharacters()) {
            int level = Integer.parseInt(sub[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.SEAL, MobSkillFactory.getMobSkill(120, level));
            } else {
                player.dropMessage("Player is not on.");
            } 
            }
                                } else if (sub[0].equals("weaken")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            int level = Integer.parseInt(sub[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.WEAKEN, MobSkillFactory.getMobSkill(122, level));
            } else {
                player.dropMessage("Player is not on.");
            }
                                } else if (sub[0].equals("weakenmap")) {
            for (MapleCharacter victim : player.getMap().getCharacters()) {
            int level = Integer.parseInt(sub[2]);
            if (victim != null) {
                victim.setChair(0);
                victim.getClient().getSession().write(MaplePacketCreator.cancelChair(-1));
                victim.getMap().broadcastMessage(victim, MaplePacketCreator.showChair(victim.getId(), 0), false);
                victim.giveDebuff(MapleDisease.WEAKEN, MobSkillFactory.getMobSkill(122, level));
            } else {
                player.dropMessage("Player is not on.");
            } 
            }            
            } else if (sub[0].equals("speak")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            if (victim != null) {
                String text = StringUtil.joinStringFrom(sub, 2);
                victim.getMap().broadcastMessage(MaplePacketCreator.getChatText(victim.getId(), text, false, 0));
            } else {
                player.dropMessage("Player not found");
            }
            } 
            else if (sub[0].equals("clock")) {
            player.getMap().broadcastMessage(MaplePacketCreator.getClock(getOptionalIntArg(sub, 1, 60)));
        } else if (sub[0].equals("cleardrops")) {
            player.getMap().clearDrops(player);
        } else if (sub[0].equals("gmchat")) {
            String message = joinStringFrom(sub, 1);
            Server.getInstance().gmChat(player.getName() + " : " + message, null);
        } else if (sub[0].equals("warpoxtop") || sub[0].equals("warpoxleft") || sub[0].equals("warpoxright") || sub[0].equals("warpoxmiddle")) {
            if (player.getMap().getId() == 109020001) {
                if (sub[0].equals("warpoxtop")) {
                    for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                        MapleCharacter person = (MapleCharacter) wrappedPerson;
                        if (person.getPosition().y <= -206 && !person.isGM())
                            person.changeMap(person.getMap().getReturnMap(),person.getMap().getReturnMap().getPortal(0));
                    }
                    player.dropMessage("Comienzo de la pagina.");
                } else if (sub[0].equals("warpoxleft")) {
                    for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                        MapleCharacter person = (MapleCharacter) wrappedPerson;
                        if (person.getPosition().y > -206 && person.getPosition().y <= 334 && person.getPosition().x >= -952 && person.getPosition().x <= -308 && !person.isGM())
                            person.changeMap(person.getMap().getReturnMap(),person.getMap().getReturnMap().getPortal(0));
                    }
                    player.dropMessage("Izquierda deformada.");
                } else if (sub[0].equals("warpoxright")) {
                    for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                        MapleCharacter person = (MapleCharacter) wrappedPerson;
                        if (person.getPosition().y > -206 && person.getPosition().y <= 334 && person.getPosition().x >= -142 && person.getPosition().x <= 502 && !person.isGM())
                            person.changeMap(person.getMap().getReturnMap(),person.getMap().getReturnMap().getPortal(0));
                    }
                    player.dropMessage("La derecha se tambaleo.");
                } else if (sub[0].equals("warpoxmiddle")) {
                    for (MapleMapObject wrappedPerson : player.getMap().getCharactersAsMapObjects()) {
                        MapleCharacter person = (MapleCharacter) wrappedPerson;
                        if (person.getPosition().y > -206 && person.getPosition().y <= 274 && person.getPosition().x >= -308 && person.getPosition().x <= -142 && !person.isGM())
                            person.changeMap(person.getMap().getReturnMap(),person.getMap().getReturnMap().getPortal(0));
                    }
                    player.dropMessage("Medio deformado.");
                }
            } else {
                player.dropMessage("Estos comandos solo pueden utilizarse en el mapa OX.");
            }
        } else if (sub[0].equals("dc")) {
            MapleCharacter victim = c.getWorldServer().getPlayerStorage().getCharacterByName(sub[1]);
            if (victim == null) {
                victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
                if (victim == null) {
                    victim = player.getMap().getCharacterByName(sub[1]);
                    if (victim != null) {
                        try {//sometimes bugged because the map = null
                            victim.getClient().getSession().close();
                            player.getMap().removePlayer(victim);
                        } catch (Exception e) {
                        }
                    } else {

                        return true;
                    }
                }
            }
            if (player.gmLevel() < victim.gmLevel()) {
                victim = player;
            }
            victim.getClient().disconnect(false, false);
        } else if (sub[0].equals("exprate")) {
            c.getWorldServer().setExpRate(Integer.parseInt(sub[1]));
            for (MapleCharacter mc : c.getWorldServer().getPlayerStorage().getAllCharacters()) {
                mc.setRates();
            }
        } else if (sub[0].equals("fame")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            victim.setFame(Integer.parseInt(sub[2]));
            victim.updateSingleStat(MapleStat.FAME, victim.getFame());
        } else if (sub[0].equals("giftnx")) {
            cserv.getPlayerStorage().getCharacterByName(sub[1]).getCashShop().gainCash(1, Integer.parseInt(sub[2]));
            player.message("Hecho, ya le ha dado su Cash.");
        } else if (sub[0].equals("gmshop")) {
            MapleShopFactory.getInstance().getShop(1337).sendShop(c);
        }  else if (sub[0].equals("viewRecentChat")) {
                        ChatLog.getInstance().viewRecentAsDrop(player.getClient());
                    } else if (sub[0].equalsIgnoreCase("searchChat")) {
                        ChatLog.getInstance().searchLog(sub[1], player.getClient());
                    }else if (sub[0].equals("heal")) {
            player.setHpMp(30000);
        }
   // }
    else if (sub[0].equals("id")) {
            try {
                try (BufferedReader dis = new BufferedReader(new InputStreamReader(new URL("http://www.mapletip.com/search_java.php?search_value=" + sub[1] + "&check=true").openConnection().getInputStream()))) {
                    String s;
                    while ((s = dis.readLine()) != null) {
                        player.dropMessage(s);
                    }
                }
            } catch (Exception e) {
            }
        } else if (sub[0].equals("item") || sub[0].equals("drop")) {
            int itemId = Integer.parseInt(sub[1]);
            short quantity = 1;
            try {
                quantity = Short.parseShort(sub[2]);
            } catch (Exception e) {
            }
            if (sub[0].equals("item")) {
                int petid = -1;
                if (ItemConstants.isPet(itemId)) {
                    petid = MaplePet.createPet(itemId);
                }
                MapleInventoryManipulator.addById(c, itemId, quantity, player.getName(), petid, -1);
            } else {
                Item toDrop;
                if (MapleItemInformationProvider.getInstance().getInventoryType(itemId) == MapleInventoryType.EQUIP) {
                    toDrop = MapleItemInformationProvider.getInstance().getEquipById(itemId);
                } else {
                    toDrop = new Item(itemId, (byte) 0, quantity);
                }
                c.getPlayer().getMap().spawnItemDrop(c.getPlayer(), c.getPlayer(), toDrop, c.getPlayer().getPosition(), true, true);
            }
        } else if (sub[0].equals("job")) {
            player.changeJob(MapleJob.getById(Integer.parseInt(sub[1])));
            player.equipChanged();
        } else if (sub[0].equals("kill")) {
            if (sub.length >= 2) {
                cserv.getPlayerStorage().getCharacterByName(sub[1]).setHpMp(0);
            }
        } else if (sub[0].equals("killall")) {
            List<MapleMapObject> monsters = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER));
            MapleMap map = player.getMap();
            for (MapleMapObject monstermo : monsters) {
                MapleMonster monster = (MapleMonster) monstermo;
                map.killMonster(monster, player, true);
                monster.giveExpToCharacter(player, monster.getExp() * c.getPlayer().getExpRate(), true, 1);
            }
            player.dropMessage("Killed " + monsters.size() + " monsters.");
        } else if (sub[0].equals("monsterdebug")) {
            List<MapleMapObject> monsters = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.MONSTER));
            for (MapleMapObject monstermo : monsters) {
                MapleMonster monster = (MapleMonster) monstermo;
                player.message("Monster ID: " + monster.getId());
            }
        } else if (sub[0].equals("unbug")) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.enableActions());
        } else if (sub[0].equals("level")) {
            player.setLevel(Integer.parseInt(sub[1]));
            player.gainExp(-player.getExp(), false, false);
            player.updateSingleStat(MapleStat.LEVEL, player.getLevel());
        } else if (sub[0].equals("levelperson")) {
            MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
            victim.setLevel(Integer.parseInt(sub[2]));
            victim.gainExp(-victim.getExp(), false, false);
            victim.updateSingleStat(MapleStat.LEVEL, victim.getLevel());
        } else if (sub[0].equals("levelpro")) {
            while (player.getLevel() < Math.min(255, Integer.parseInt(sub[1]))) {
                player.levelUp(false);
            }
        } else if (sub[0].equals("levelup")) {
            player.levelUp(false);
        } else if (sub[0].equals("maxstat")) {
            final String[] s = {"setall", String.valueOf(Short.MAX_VALUE)};
            executeGMCommand(c, s, heading);
            player.setLevel(255);
            player.setFame(13337);
            player.setMaxHp(30000);
            player.setMaxMp(30000);
            player.updateSingleStat(MapleStat.LEVEL, 255);
            player.updateSingleStat(MapleStat.FAME, 13337);
            player.updateSingleStat(MapleStat.MAXHP, 30000);
            player.updateSingleStat(MapleStat.MAXMP, 30000);
        } else if (sub[0].equals("maxskills")) {
            for (MapleData skill_ : MapleDataProviderFactory.getDataProvider(new File(System.getProperty("wzpath") + "/" + "String.wz")).getData("Skill.img").getChildren()) {
                try {
                    Skill skill = SkillFactory.getSkill(Integer.parseInt(skill_.getName()));
                    player.changeSkillLevel(skill, (byte) skill.getMaxLevel(), skill.getMaxLevel(), -1);
                } catch (NumberFormatException nfe) {
                    break;
                } catch (NullPointerException npe) {
                    continue;
                }
            }
        } else if (sub[0].equals("mesoperson")) {
            cserv.getPlayerStorage().getCharacterByName(sub[1]).gainMeso(Integer.parseInt(sub[2]), true);
        } else if (sub[0].equals("mesos")) {
            player.gainMeso(Integer.parseInt(sub[1]), true);
        } else if (sub[0].equals("notice")) {
            Server.getInstance().broadcastMessage(player.getWorld(), MaplePacketCreator.serverNotice(6, "[Notice] " + joinStringFrom(sub, 1)));
        } else if (sub[0].equals("openportal")) {
            player.getMap().getPortal(sub[1]).setPortalState(true);
        } else if (sub[0].equals("closeportal")) {
            player.getMap().getPortal(sub[1]).setPortalState(false);
        } else if (sub[0].equals("startevent")) {
            for (MapleCharacter chr : player.getMap().getCharacters()) {
                player.getMap().startEvent(chr);
            }
            c.getChannelServer().setEvent(null);
        } else if (sub[0].equals("scheduleevent")) {
            if (c.getPlayer().getMap().hasEventNPC()) {
                switch (sub[1]) {
                    case "treasure":
                        c.getChannelServer().setEvent(new MapleEvent(109010000, 50));
                        break;
                    case "ox":
                        c.getChannelServer().setEvent(new MapleEvent(109020001, 50));
                        srv.broadcastMessage(player.getWorld(), MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()));
                        break;
                    case "ola":
                        c.getChannelServer().setEvent(new MapleEvent(109030101, 50)); // Wrong map but still Ola Ola
                        srv.broadcastMessage(player.getWorld(), MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()));
                        break;
                    case "fitness":
                        c.getChannelServer().setEvent(new MapleEvent(109040000, 50));
                        srv.broadcastMessage(player.getWorld(), MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()));
                        break;
                    case "snowball":
                        c.getChannelServer().setEvent(new MapleEvent(109060001, 50));
                        srv.broadcastMessage(player.getWorld(), MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()));
                        break;
                    case "coconut":
                        c.getChannelServer().setEvent(new MapleEvent(109080000, 50));
                        srv.broadcastMessage(player.getWorld(), MaplePacketCreator.serverNotice(0, "Hello Scania let's play an event in " + player.getMap().getMapName() + " CH " + c.getChannel() + "! " + player.getMap().getEventNPC()));
                        break;
                    default:
                        player.message("Wrong Syntax: /scheduleevent treasure, ox, ola, fitness, snowball or coconut");
                        break;
                }
            } else {
                player.message("You can only use this command in the following maps: 60000, 104000000, 200000000, 220000000");
            }

        } else if (sub[0].equals("online")) {
            for (Channel ch : srv.getChannelsFromWorld(player.getWorld())) {
                String s = "Characters online (Channel " + ch.getId() + " Online: " + ch.getPlayerStorage().getAllCharacters().size() + ") : ";
                if (ch.getPlayerStorage().getAllCharacters().size() < 50) {
                    for (MapleCharacter chr : ch.getPlayerStorage().getAllCharacters()) {
                        s += MapleCharacter.makeMapleReadable(chr.getName()) + ", ";
                    }
                    player.dropMessage(s.substring(0, s.length() - 2));
                }
            }
        } else if (sub[0].equals("pap")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8500001), player.getPosition());
        } else if (sub[0].equals("pianus")) {
            player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8510000), player.getPosition());
        } else if (sub[0].equalsIgnoreCase("search")) {
            StringBuilder sb = new StringBuilder();
            if (sub.length > 2) {
                String search = joinStringFrom(sub, 2);
                long start = System.currentTimeMillis();//for the lulz
                MapleData data = null;
                MapleDataProvider dataProvider = MapleDataProviderFactory.getDataProvider(new File("wz/String.wz"));
                if (!sub[1].equalsIgnoreCase("ITEM")) {
                    if (sub[1].equalsIgnoreCase("NPC")) {
                        data = dataProvider.getData("Npc.img");
                    } else if (sub[1].equalsIgnoreCase("MOB") || sub[1].equalsIgnoreCase("MONSTER")) {
                        data = dataProvider.getData("Mob.img");
                    } else if (sub[1].equalsIgnoreCase("SKILL")) {
                        data = dataProvider.getData("Skill.img");
                    } else if (sub[1].equalsIgnoreCase("MAP")) {
                        sb.append("#bUse the '/m' command to find a map. If it finds a map with the same name, it will warp you to it.");
                    } else {
                        sb.append("#bInvalid search.\r\nSyntax: '/search [type] [name]', where [type] is NPC, ITEM, MOB, or SKILL.");
                    }
                    if (data != null) {
                        String name;
                        for (MapleData searchData : data.getChildren()) {
                            name = MapleDataTool.getString(searchData.getChildByPath("name"), "NO-NAME");
                            if (name.toLowerCase().contains(search.toLowerCase())) {
                                sb.append("#b").append(Integer.parseInt(searchData.getName())).append("#k - #r").append(name).append("\r\n");
                            }
                        }
                    }
                } else {
                    for (Pair<Integer, String> itemPair : MapleItemInformationProvider.getInstance().getAllItems()) {
                        if (sb.length() < 32654) {//ohlol
                            if (itemPair.getRight().toLowerCase().contains(search.toLowerCase())) {
                                //#v").append(id).append("# #k- 
                                sb.append("#b").append(itemPair.getLeft()).append("#k - #r").append(itemPair.getRight()).append("\r\n");
                            }
                        } else {
                            sb.append("#bCouldn't load all items, there are too many results.\r\n");
                            break;
                        }
                    }
                }
                if (sb.length() == 0) {
                    sb.append("#bNo ").append(sub[1].toLowerCase()).append("s found.\r\n");
                }

                sb.append("\r\n#kLoaded within ").append((double) (System.currentTimeMillis() - start) / 1000).append(" seconds.");//because I can, and it's free

            } else {
                sb.append("#bInvalid search.\r\nSyntax: '/search [type] [name]', where [type] is NPC, ITEM, MOB, or SKILL.");
            }
            c.announce(MaplePacketCreator.getNPCTalk(9010000, (byte) 0, sb.toString(), "00 00", (byte) 0));
        } else if (sub[0].equals("servermessage")) {
            c.getWorldServer().setServerMessage(joinStringFrom(sub, 1));
        } else if (sub[0].equals("warpsnowball")) {
            for (MapleCharacter chr : player.getMap().getCharacters()) {
                chr.changeMap(109060000, chr.getTeam());
            }
        } else if (sub[0].equals("setall")) {
            final int x = Short.parseShort(sub[1]);
            player.setStr(x);
            player.setDex(x);
            player.setInt(x);
            player.setLuk(x);
            player.updateSingleStat(MapleStat.STR, x);
            player.updateSingleStat(MapleStat.DEX, x);
            player.updateSingleStat(MapleStat.INT, x);
            player.updateSingleStat(MapleStat.LUK, x);
        } else if (sub[0].equals("sp")) {
            player.setRemainingSp(Integer.parseInt(sub[1]));
            player.updateSingleStat(MapleStat.AVAILABLESP, player.getRemainingSp());
        } else if (sub[0].equals("unban")) {
            try {
                try (PreparedStatement p = DatabaseConnection.getConnection().prepareStatement("UPDATE accounts SET banned = -1 WHERE id = " + MapleCharacter.getIdByName(sub[1]))) {
                    p.executeUpdate();
                }
            } catch (Exception e) {
                player.message("Failed to unban " + sub[1]);
                return true;
            }
            player.message("Unbanned " + sub[1]);
        } else {
            return false;
        }
        return true;
    }

    public static void executeAdminCommand(MapleClient c, String[] sub, char heading) {
        MapleCharacter player = c.getPlayer();
        Channel cserv = c.getChannelServer();
        switch (sub[0]) {
            case "horntail":
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8810026), player.getPosition());
                break;
            case "packet":
                player.getMap().broadcastMessage(MaplePacketCreator.customPacket(joinStringFrom(sub, 1)));
                break;
            case "warpworld":
                Server server = Server.getInstance();
                byte worldb = Byte.parseByte(sub[1]);
                if (worldb <= (server.getWorlds().size() - 1)) {
                    try {
                        String[] socket = server.getIP(worldb, c.getChannel()).split(":");
                        c.getWorldServer().removePlayer(player);
                        player.getMap().removePlayer(player);//LOL FORGOT THIS ><                    
                        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
                        player.setWorld(worldb);
                        player.saveToDB();//To set the new world :O (true because else 2 player instances are created, one in both worlds)
                        c.announce(MaplePacketCreator.getChannelChange(InetAddress.getByName(socket[0]), Integer.parseInt(socket[1])));
                    } catch (UnknownHostException | NumberFormatException ex) {
                        player.message("Error when trying to change worlds, are you sure the world you are trying to warp to has the same amount of channels?");
                    }

                } else {
                    player.message("Invalid world; highest number available: " + (server.getWorlds().size() - 1));
                }
                break;
                case "pmob":            
            int npcId = Integer.parseInt(sub[1]);
            int mobTime = Integer.parseInt(sub[2]);
            int xpos = player.getPosition().x;
            int ypos = player.getPosition().y;
            int fh = player.getMap().getFootholds().findBelow(player.getPosition()).getId();
            if (sub[2] == null) {
                mobTime = 0;
            }
            MapleMonster mob = MapleLifeFactory.getMonster(npcId);
            if (mob != null && !mob.getName().equals("MISSINGNO")) {
                mob.setPosition(player.getPosition());
                mob.setCy(ypos);
                mob.setRx0(xpos + 50);
                mob.setRx1(xpos - 50);
                mob.setFh(fh);
                try {
                    Connection con = DatabaseConnection.getConnection();
                    PreparedStatement ps = con.prepareStatement("INSERT INTO spawns ( idd, f, fh, cy, rx0, rx1, type, x, y, mid, mobtime ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
                    ps.setInt(1, npcId);
                    ps.setInt(2, 0);
                    ps.setInt(3, fh);
                    ps.setInt(4, ypos);
                    ps.setInt(5, xpos + 50);
                    ps.setInt(6, xpos - 50);
                    ps.setString(7, "m");
                    ps.setInt(8, xpos);
                    ps.setInt(9, ypos);
                    ps.setInt(10, player.getMapId());
                    ps.setInt(11, mobTime);
                    ps.executeUpdate();
                } catch (SQLException e) {
                    player.dropMessage("Failed to save MOB to the database");
                }
                player.getMap().addMonsterSpawn(mob, mobTime, 0);
            } else {
                player.dropMessage("You have entered an invalid Mob-Id");
            }
                break;
            case "saveall"://fyi this is a stupid command
                for (World world : Server.getInstance().getWorlds()) {
                    for (MapleCharacter chr : world.getPlayerStorage().getAllCharacters()) {
                        chr.saveToDB();
                    }
                }
                break;
                case "proitem":
                        if (sub.length < 3) {
                                player.yellowMessage("Syntax: !proitem <itemid> <statvalue>");
                                break;
                        }
                        
                        int itemid = 0;
                        short multiply = 0;

                        itemid = Integer.parseInt(sub[1]);
                        multiply = Short.parseShort(sub[2]);

                        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                        Item it = ii.getEquipById(itemid);
                        MapleInventoryType type = ii.getInventoryType(itemid);
                        if (type.equals(MapleInventoryType.EQUIP)) {
                                hardsetItemStats((Equip) it, multiply);
                                MapleInventoryManipulator.addFromDrop(c, it);

                        } else {
                                player.dropMessage("Make sure it's an equippable item.");
                        }
                        break;
            case "npc":
                if (sub.length < 1) {
                    break;
                }
                MapleNPC npc = MapleLifeFactory.getNPC(Integer.parseInt(sub[1]));
                if (npc != null) {
                    npc.setPosition(player.getPosition());
                    npc.setCy(player.getPosition().y);
                    npc.setRx0(player.getPosition().x + 50);
                    npc.setRx1(player.getPosition().x - 50);
                    npc.setFh(player.getMap().getFootholds().findBelow(c.getPlayer().getPosition()).getId());
                    player.getMap().addMapObject(npc);
                    player.getMap().broadcastMessage(MaplePacketCreator.spawnNPC(npc));
                }
                break;
            case "jobperson": {
                MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
                victim.changeJob(MapleJob.getById(Integer.parseInt(sub[2])));
                player.equipChanged();
                break;
            }
            case "pinkbean":
                player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(8820009), player.getPosition());
                break;
            case "playernpc":
                player.playerNPC(c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]), Integer.parseInt(sub[2]));
                break;
            case "setgmlevel": {
                MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(sub[1]);
                victim.setGM(Integer.parseInt(sub[2]));
                player.message("Done.");
                victim.getClient().disconnect(false, false);
                break;
            }
            case "shutdown":
            case "shutdownnow":
                int time = 60000;
                if (sub[0].equals("shutdownnow")) {
                    time = 1;
                } else if (sub.length > 1) {
                    time *= Integer.parseInt(sub[1]);
                }
                TimerManager.getInstance().schedule(Server.getInstance().shutdown(false), time);
                break;
            case "sql": {
                final String query = Commands.joinStringFrom(sub, 1);
                try {
                    try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query)) {
                        ps.executeUpdate();
                    }
                    player.message("Done " + query);
                } catch (SQLException e) {
                    player.message("Query Failed: " + query);
                }
                break;
            }
            case "sqlwithresult": {
                String name = sub[1];
                final String query = Commands.joinStringFrom(sub, 2);
                try {
                    try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(query); ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            player.dropMessage(String.valueOf(rs.getObject(name)));
                        }
                    }
                } catch (SQLException e) {
                    player.message("Query Failed: " + query);
                }
                break;
            } /*case "shutdownworld": {
            int time;
            if (sub.length > 1) {
                time = Integer.parseInt(sub[1]) * 60000;
            }
            Commands.forcePersisting();
            c.getChannelServer().shutdown();
            c.getChannelServer().saveAll();
                    }
            break;*/
            case "worldtrip": {
                    MapleCharacter victim = cserv.getPlayerStorage().getCharacterByName(sub[1]);
                    for (int i = 1; i <= 10; i++) {
                        MapleMap target = cserv.getMapFactory().getMap(200000000);
                        MaplePortal targetPortal = target.getPortal(0);
                        victim.changeMap(target, targetPortal);
                        MapleMap target1 = cserv.getMapFactory().getMap(102000000);
                        MaplePortal targetPortal1 = target.getPortal(0);
                        victim.changeMap(target1, targetPortal1);
                        MapleMap target2 = cserv.getMapFactory().getMap(103000000);
                        MaplePortal targetPortal2 = target.getPortal(0);
                        victim.changeMap(target2, targetPortal2);
                        MapleMap target3 = cserv.getMapFactory().getMap(100000000);
                        MaplePortal targetPortal3 = target.getPortal(0);
                        victim.changeMap(target3, targetPortal3);
                        MapleMap target4 = cserv.getMapFactory().getMap(200000000);
                        MaplePortal targetPortal4 = target.getPortal(0);
                        victim.changeMap(target4, targetPortal4);
                        MapleMap target5 = cserv.getMapFactory().getMap(211000000);
                        MaplePortal targetPortal5 = target.getPortal(0);
                        victim.changeMap(target5, targetPortal5);
                        MapleMap target6 = cserv.getMapFactory().getMap(230000000);
                        MaplePortal targetPortal6 = target.getPortal(0);
                        victim.changeMap(target6, targetPortal6);
                        MapleMap target7 = cserv.getMapFactory().getMap(222000000);
                        MaplePortal targetPortal7 = target.getPortal(0);
                        victim.changeMap(target7, targetPortal7);
                        MapleMap target8 = cserv.getMapFactory().getMap(251000000);
                        MaplePortal targetPortal8 = target.getPortal(0);
                        victim.changeMap(target8, targetPortal8);
                        MapleMap target9 = cserv.getMapFactory().getMap(220000000);
                        MaplePortal targetPortal9 = target.getPortal(0);
                        victim.changeMap(target9, targetPortal9);
                        MapleMap target10 = cserv.getMapFactory().getMap(221000000);
                        MaplePortal targetPortal10 = target.getPortal(0);
                        victim.changeMap(target10, targetPortal10);
                        MapleMap target11 = cserv.getMapFactory().getMap(240000000);
                        MaplePortal targetPortal11 = target.getPortal(0);
                        victim.changeMap(target11, targetPortal11);
                        MapleMap target12 = cserv.getMapFactory().getMap(600000000);
                        MaplePortal targetPortal12 = target.getPortal(0);
                        victim.changeMap(target12, targetPortal12);
                        MapleMap target13 = cserv.getMapFactory().getMap(800000000);
                        MaplePortal targetPortal13 = target.getPortal(0);
                        victim.changeMap(target13, targetPortal13);
                        MapleMap target14 = cserv.getMapFactory().getMap(680000000);
                        MaplePortal targetPortal14 = target.getPortal(0);
                        victim.changeMap(target14, targetPortal14);
                        MapleMap target15 = cserv.getMapFactory().getMap(105040300);
                        MaplePortal targetPortal15 = target.getPortal(0);
                        victim.changeMap(target15, targetPortal15);
                        MapleMap target16 = cserv.getMapFactory().getMap(990000000);
                        MaplePortal targetPortal16 = target.getPortal(0);
                        victim.changeMap(target16, targetPortal16);
                        MapleMap target17 = cserv.getMapFactory().getMap(100000001);
                        MaplePortal targetPortal17 = target.getPortal(0);
                        victim.changeMap(target17, targetPortal17);
                    }
                    victim.changeMap(c.getPlayer().getMap(), c.getPlayer().getMap().findClosestSpawnpoint(
                            c.getPlayer().getPosition()));       
            }
            break;
            case "itemvac":
                List<MapleMapObject> items = player.getMap().getMapObjectsInRange(player.getPosition(), Double.POSITIVE_INFINITY, Arrays.asList(MapleMapObjectType.ITEM));
                for (MapleMapObject item : items) {
                    MapleMapItem mapitem = (MapleMapItem) item;
                    if (!MapleInventoryManipulator.addFromDrop(c, mapitem.getItem(), true)) {
                        continue;
                    }
                    mapitem.setPickedUp(true);
                    player.getMap().broadcastMessage(MaplePacketCreator.removeItemFromMap(mapitem.getObjectId(), 2, player.getId()), mapitem.getPosition());
                    player.getMap().removeMapObject(item);

                }
                break;
            case "zakum":
                player.getMap().spawnFakeMonsterOnGroundBelow(MapleLifeFactory.getMonster(8800000), player.getPosition());
                for (int x = 8800003; x < 8800011; x++) {
                    player.getMap().spawnMonsterOnGroudBelow(MapleLifeFactory.getMonster(x), player.getPosition());
                }
                break;
            default:
                player.yellowMessage("Command " + heading + sub[0] + " does not exist.");
                break;
        }
    }
    
    private static Runnable persister;
    private static List<Pair<MapleCharacter, String>> gmlog = new LinkedList<Pair<MapleCharacter, String>>();

    private static String joinStringFrom(String arr[], int start) {
        StringBuilder builder = new StringBuilder();
        for (int i = start; i < arr.length; i++) {
            builder.append(arr[i]);
            if (i != arr.length - 1) {
                builder.append(" ");
            }
        }
        return builder.toString();
    }

    private static void hardsetItemStats(Equip equip, short stat) {
            equip.setStr(stat);
            equip.setDex(stat);
            equip.setInt(stat);
            equip.setLuk(stat);
            equip.setMatk(stat);
            equip.setWatk(stat);
            equip.setAcc(stat);
            equip.setAvoid(stat);
            equip.setJump(stat);
            equip.setSpeed(stat);
            equip.setWdef(stat);
            equip.setMdef(stat);
            equip.setHp(stat);
            equip.setMp(stat);
            
            byte flag = equip.getFlag();
            flag |= ItemConstants.UNTRADEABLE;
            equip.setFlag(flag);
        }

    public static void forcePersisting() {
        persister.run();
    }

            static {
        persister = new PersistingTask();
        TimerManager.getInstance().register(persister, 62000);
    }
            
            public static class PersistingTask implements Runnable {

        @Override
        public void run() {
            synchronized (gmlog) {
                Connection con = (Connection) DatabaseConnection.getConnection();
                try {
                    PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO gmlog (cid, command) VALUES (?, ?)");
                    for (Pair<MapleCharacter, String> logentry : gmlog) {
                        ps.setInt(1, logentry.getLeft().getId());
                        ps.setString(2, logentry.getRight());
                        ps.executeUpdate();
                    }
                    ps.close();
                } catch (SQLException e) {
                    System.out.println("Error persisting cheatlog" + e);
                }
                gmlog.clear();
            }
        }
}
            public static int getOptionalIntArg(String splitted[], int position, int def) {
        if (splitted.length > position) {
            try {
                return Integer.parseInt(splitted[position]);
            } catch (NumberFormatException nfe) {
                return def;
            }
        }
        return def;
    }
}