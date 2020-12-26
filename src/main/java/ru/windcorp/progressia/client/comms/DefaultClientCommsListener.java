package ru.windcorp.progressia.client.comms;

import java.io.IOException;

import ru.windcorp.jputil.chars.StringUtil;
import ru.windcorp.progressia.client.Client;
import ru.windcorp.progressia.client.graphics.world.EntityAnchor;
import ru.windcorp.progressia.common.comms.CommsListener;
import ru.windcorp.progressia.common.comms.packets.Packet;
import ru.windcorp.progressia.common.util.crash.CrashReports;
import ru.windcorp.progressia.common.world.PacketSetLocalPlayer;
import ru.windcorp.progressia.common.world.PacketWorldChange;
import ru.windcorp.progressia.common.world.entity.EntityData;

// TODO refactor with no mercy
public class DefaultClientCommsListener implements CommsListener {

	private final Client client;

	public DefaultClientCommsListener(Client client) {
		this.client = client;
	}

	@Override
	public void onPacketReceived(Packet packet) {
		if (packet instanceof PacketWorldChange) {
			((PacketWorldChange) packet).apply(
					getClient().getWorld().getData()
			);
		} else if (packet instanceof PacketSetLocalPlayer) {
			setLocalPlayer((PacketSetLocalPlayer) packet);
		}
	}

	private void setLocalPlayer(PacketSetLocalPlayer packet) {
		EntityData entity = getClient().getWorld().getData().getEntity(
				packet.getEntityId()
		);
		
		if (entity == null) {
			CrashReports.report(
					null,
					"Player entity with ID %s not found",
					new String(StringUtil.toFullHex(packet.getEntityId()))
			);
		}

		getClient().setLocalPlayer(entity);
		getClient().getCamera().setAnchor(new EntityAnchor(
				getClient().getWorld().getEntityRenderable(entity)
		));
	}

	@Override
	public void onIOError(IOException reason) {
		// TODO implement
	}

	public Client getClient() {
		return client;
	}

}
