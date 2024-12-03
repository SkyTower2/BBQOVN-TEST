package com.me.ml.bluetooth_kit.channel;

public class ChannelStateBlock {

    public ChannelState state;

    public ChannelEvent event;

    public IChannelStateHandler handler;

    public ChannelStateBlock(ChannelState state, ChannelEvent event, IChannelStateHandler handler) {
        this.state = state;
        this.event = event;
        this.handler = handler;
    }

}
