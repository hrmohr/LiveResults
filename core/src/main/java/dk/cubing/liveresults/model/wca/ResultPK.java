/*
 * Copyright (C) 2011 Mads Mohr Christensen, <hr.mohr@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dk.cubing.liveresults.model.wca;

import java.io.Serializable;

public class ResultPK implements Serializable {

    private String competitionId;
    private String eventId;
    private String roundId;
    private String personId;

    public ResultPK(String competitionId, String eventId, String roundId, String personId) {
        this.competitionId = competitionId;
        this.eventId = eventId;
        this.roundId = roundId;
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResultPK that = (ResultPK) o;

        if (competitionId != null ? !competitionId.equals(that.competitionId) : that.competitionId != null)
            return false;
        if (eventId != null ? !eventId.equals(that.eventId) : that.eventId != null) return false;
        if (personId != null ? !personId.equals(that.personId) : that.personId != null) return false;
        if (roundId != null ? !roundId.equals(that.roundId) : that.roundId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = competitionId != null ? competitionId.hashCode() : 0;
        result = 31 * result + (eventId != null ? eventId.hashCode() : 0);
        result = 31 * result + (roundId != null ? roundId.hashCode() : 0);
        result = 31 * result + (personId != null ? personId.hashCode() : 0);
        return result;
    }
}
