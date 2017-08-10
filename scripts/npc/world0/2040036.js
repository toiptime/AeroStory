importPackage(Packages.tools);
importPackage(java.awt);

var status;
var partyLdr;
var chatState;
var party;
var preamble;

function start() {
    status = -1;
    playerStatus = cm.isLeader();
    preamble = null;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0 && status == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
            if (playerStatus) { // party leader
                if (status == 0) {
                    var eim = cm.getPlayer().getEventInstance();
                    party = eim.getPlayers();
                    preamble = eim.getProperty("leader1stpreamble");
                    if (preamble == null) {
                        cm.sendNext("Hello, and welcome to the first stage of Ludibrium PQ. There are #r25#k monsters in here, kill them to retrieve the #b#t4001022#'s#k, and give them to me, and I will open the portal.");
                        eim.setProperty("leader1stpreamble","done");
                        cm.dispose();
                    }
                    else { // check how many they have compared to number of party members
                                    // check for stage completed
                                    var complete = eim.getProperty("1stageclear");
                                    if (complete != null) {
                                        cm.sendNext("Please proceed in the Party Quest, the portal opened!");
                                        cm.dispose();
                                    }
                                    else {
                            if (cm.itemQuantity(4001022) != 25) {
                                cm.sendNext("I'm sorry, but you do not have all 25 #b#t4001022#'s#k needed to clear this stage.");
                                cm.dispose();
                            }
                            else {
                                cm.sendNext("Congratulations on clearing the first stage! I will open the portal now.");
                                clear(1,eim,cm);
                                cm.givePartyExp(3000, party);
                                cm.gainItem(4001022, -25);
                                cm.dispose();
                            }
                        }
                    }
                }
            }
            else { // non leader
                var eim = cm.getPlayer().getEventInstance();
                pstring = "member1stpreamble" + cm.getPlayer().getId().toString();
                preamble = eim.getProperty(pstring);
                if (status == 0 && preamble == null) {
                    var qstring = "member1st" + cm.getPlayer().getId().toString();
                    var question = eim.getProperty(qstring);
                    if (question == null) {
                        qstring = "FUCK";
                    }
                    cm.sendNext("Hello, and welcome to the first stage of Ludibrium PQ. There are #r25#k monsters in here, kill them to retrieve the #b#t4001022#'s#k, and give them to me, and I will open the portal.");

                }
                else if (status == 0) {// otherwise
                                // check for stage completed
                                var complete = eim.getProperty("1stageclear");
                                if (complete != null) {
                                    cm.sendNext("Please proceed in the Party Quest, the portal opened!");
                                    cm.dispose();
                                }
                                else {
                            cm.sendOk("Please talk to me after you've completed the stage.");
                            cm.dispose();
                    }
                }
                else if (status == 1) {
                    if (preamble == null) {
                        cm.sendOk("Ok, best of luck to you!");
                        cm.dispose();
                    }
                    else { // shouldn't happen, if it does then just dispose
                        cm.dispose();
                    }

                }
                else if (status == 2) { // preamble completed
                    eim.setProperty(pstring,"done");
                    cm.dispose();
                }
                else { // shouldn't happen, but still...
                    eim.setProperty(pstring,"done"); // just to be sure
                    cm.dispose();
                }
            }
        }
    }

function clear(stage, eim, cm) {
eim.setProperty("1stageclear","true");
var packetef = MaplePacketCreator.showEffect("quest/party/clear");
var packetsnd = MaplePacketCreator.playSound("Party1/Clear");
var packetglow = MaplePacketCreator.environmentChange("gate",2);
var map = eim.getMapInstance(cm.getPlayer().getMapId());
map.broadcastMessage(packetef);
map.broadcastMessage(packetsnd);
map.broadcastMessage(packetglow);
var mf = eim.getMapFactory();
map = mf.getMap(922010100 + stage * 100);
cm.givePartyExp(270, party);
}