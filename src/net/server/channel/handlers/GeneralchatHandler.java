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
License.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.server.channel.handlers;

import client.ChatLog;
import client.MapleCharacter;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;
import client.MapleClient;
import client.command.Commands;

public final class GeneralchatHandler extends net.AbstractMaplePacketHandler {

    public final void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {
        String s = slea.readMapleAsciiString();
        MapleCharacter chr = c.getPlayer();
        char heading = s.charAt(0);
        ChatLog.getInstance().add("["+ChatLog.getInstance().generateTime()+"]" + " ~ " + c.getPlayer().getName() + ": " + s);
                if (ChatLog.getInstance().getChat().size() >= 500) ChatLog.getInstance().makeLog(); //change 500 to w/e
        if (heading == '/' || heading == '!' || heading == '@') {
            String[] sp = s.split(" ");
            sp[0] = sp[0].toLowerCase().substring(1);
            if (!Commands.executePlayerCommand(c, sp, heading)) {
                if (chr.isGM()) {
                    if (!Commands.executeGMCommand(c, sp, heading)) {
                        Commands.executeAdminCommand(c, sp, heading);
                    }
                }
            }
            if (chr.isDonator()) {
			//	chr.addCommandToList(s);
				if (!Commands.executePlayerCommand(c, sp, heading)) {
				   Commands.executeDonatorCommand(c, sp, heading); {
				    }
				}
            }
           // }
        } else {
            if (!chr.isHidden())
                chr.getMap().broadcastMessage(MaplePacketCreator.getChatText(chr.getId(), s, chr.isGM(), slea.readByte()));
            else
                chr.getMap().broadcastGMMessage(MaplePacketCreator.getChatText(chr.getId(), s, chr.isGM(), slea.readByte()));
        }
    }
}

