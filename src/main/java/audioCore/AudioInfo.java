/*
 * (C) Copyright 2016 Dinos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package audioCore;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.util.HashSet;
import java.util.Set;

public class AudioInfo {

    private final AudioTrack track;
    private final Set<String> skips;
    private final Member author;

    AudioInfo(AudioTrack track, Member author) {
        this.track = track;
        this.skips = new HashSet<>();
        this.author = author;
    }

    public AudioTrack getTrack() {
        return track;
    }

    public int getSkips() {
        return skips.size();
    }

    public void addSkip(User u) {
        skips.add(u.getId());
    }

    public boolean hasVoted(User u) {
        return skips.contains(u.getId());
    }

    public Member getAuthor() {
        return author;
    }

}