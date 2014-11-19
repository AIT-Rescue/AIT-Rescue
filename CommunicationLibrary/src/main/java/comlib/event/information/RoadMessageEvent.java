package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.RoadMessage;

public interface RoadMessageEvent extends MessageEvent<RoadMessage> {

    @Override
    public abstract void receivedRadio(RoadMessage msg);

    @Override
    public abstract void receivedVoice(RoadMessage msg);
}
