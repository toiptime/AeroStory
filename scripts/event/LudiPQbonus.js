/*
 * This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation. You may not use, modify
    or distribute this program under any other version of the
    GNU Affero General Public License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
-- Odin JavaScript --------------------------------------------------------------------------------
	Ludibirum Bonus PQ (copied from KPQ and tweaked severely)
-- By ---------------------------------------------------------------------------------------------
	Stereo
-- Version Info -----------------------------------------------------------------------------------
	1.1 - fixed minor problems
	1.0 - First Version by Stereo
---------------------------------------------------------------------------------------------------
**/

/*

*/

importPackage(Packages.world);
var exitMap;
var minPlayers = 5;

function init() { // Initial loading.
    exitMap = em.getChannelServer().getMapFactory().getMap(922010000);
    em.setProperty("LPQbonusOpen", "true"); // allows entrance.
    instanceId = 10;
}

function monsterValue(eim, mobId) { // Killed monster.
    return 1; // returns an amount to add onto kill count.
}

function setup() { // Invoked from "EventManager.startInstance()"
    var eim = em.newInstance("LudiPQbonus"); // adds a new instance and returns EventInstanceManager.
    var eventTime = 31 * (1000 * 2); // 1 min + 2 sec to make up for warp lag.
    em.schedule("timeOut", eim, eventTime); // invokes "timeOut" in how ever many seconds.
    eim.startEventTimer(eventTime); // Sends a clock packet and tags a timer to the players.
    return eim; // returns the new instance.
}

function playerEntry(eim, player) { // this gets looped for every player in the party.
    var map = eim.getMapInstance(922011000);
    player.changeMap(map, map.getPortal(0)); // We're now in LPQ :D
}


function playerDisconnected(eim, player) {
    var party = eim.getPlayers();
    if (eim.isLeader(player) || party.size() < minPlayers) {
        var party = eim.getPlayers();
        for (var i = 0; i < party.size(); i++)
            if (party.get(i).equals(player))
                removePlayer(eim, player);
            else
                playerExit(eim, party.get(i));
        eim.dispose();
    } else
        removePlayer(eim, player);
}

function disbandParty(eim) {
    var party = eim.getPlayers();
    for (var i = 0; i < party.size(); i++) {
        playerExit(eim, party.get(i));
    }
    eim.dispose();
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    player.changeMap(exitMap, exitMap.getPortal(0));
}

function removePlayer(eim, player) {
    eim.unregisterPlayer(player);
    player.getMap().removePlayer(player);
    player.setMap(exitMap);
}

function clearPQ(eim) {
    var party = eim.getPlayers();
    for (var i = 0; i < party.size(); i++)
        playerExit(eim, party.get(i));
    eim.dispose();
}

function allMonstersDead(eim) {
}

function cancelSchedule() {
}

function dispose() {
    em.schedule("OpenLPQbonus", 5000); // 5 seconds ?
}

function OpenLPQ() {
    em.setProperty("LPQbonusOpen", "true");
}

function timeOut(eim) {
    if (eim != null) {
        if (eim.getPlayerCount() > 0) {
            var pIter = eim.getPlayers().iterator();
            while (pIter.hasNext())
                playerExit(eim, pIter.next());
        }
        eim.dispose();
    }
}