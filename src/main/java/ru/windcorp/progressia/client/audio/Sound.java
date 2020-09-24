package ru.windcorp.progressia.client.audio;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.openal.AL11.*;

public class Sound {
    //Buffers
    private int audio;
    private int source;
    //Characteristics
    private FloatBuffer position =
            (FloatBuffer) BufferUtils.createFloatBuffer(3)
                    .put(new float[]{0.0f, 0.0f, 0.0f}).rewind();
    private FloatBuffer velocity =
            (FloatBuffer) BufferUtils.createFloatBuffer(3)
                    .put(new float[]{0.0f, 0.0f, 0.0f}).rewind();
    private float pitch = 1.0f;
    private float gain = 1.0f;

    public Sound() {
    }

    public Sound(int audio, int source) {
        setAudio(audio);
        setSource(source);
    }

    public Sound(int audio, FloatBuffer position, FloatBuffer velocity, float pitch, float gain)
    {
        setAudio(audio);
        setPosition(position);
        setVelocity(velocity);
        setPitch(pitch);
        setGain(gain);
    }

    public Sound(FloatBuffer position, FloatBuffer velocity, float pitch, float gain) {
        setPosition(position);
        setVelocity(velocity);
        setPitch(pitch);
        setGain(gain);
    }

    public void playOnce() {
        alSourcePlay(source);
    }

    public void playLoop() {
            alSourcei(source, AL_LOOPING, AL_TRUE);
            playOnce();
            alSourcei(source, AL_LOOPING, AL_FALSE);
    }

    public void stop() {
        alSourceStop(source);
    }

    public void pause() {
        alSourcePause(source);
    }

    public boolean isPlaying() {
        final int state = alGetSourcei(source, AL_SOURCE_STATE);
        return state == AL_PLAYING;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }

    public void setSource(int source) {
        this.source = source;
        alSourcei(this.source, AL_BUFFER, audio);
    }

    public int getSource() {
        return source;
    }


    //OTHER

    public void setPosition(FloatBuffer position) {
        this.position = position;
        alSourcefv(source, AL_POSITION, position);
    }

    public FloatBuffer getPosition() {
        return position;
    }


    public void setVelocity(FloatBuffer velocity) {
        alSourcefv(source, AL_VELOCITY, velocity);
        this.velocity = velocity;
    }

    public FloatBuffer getVelocity() {
        return velocity;
    }


    public void setPitch(float pitch) {
        alSourcef(source, AL_PITCH, pitch);
        this.pitch = pitch;
    }

    public float getPitch() {
        return pitch;
    }


    public void setGain(float gain) {
        alSourcef(source, AL_GAIN, gain);
        this.gain = gain;
    }

    public float getGain() {
        return gain;
    }
}