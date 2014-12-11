package comlib.event.information;

import comlib.event.MessageEvent;
import comlib.message.information.MessageRoad;

public interface EventMessageRoad extends MessageEvent<MessageRoad> {

    @Override
    public abstract void receivedRadio(MessageRoad msg);

    @Override
    public abstract void receivedVoice(MessageRoad msg);
}
