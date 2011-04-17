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

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Results")
@IdClass(ResultsPK.class)
public class Result implements Serializable {

    @Id
    private String competitionId;
    @Id
    private String eventId;
    @Id
    private String roundId;
    private String formatId;

    private String personName;
    @Id
    private String personId;
    private String personCountryId;

    private int pos;
    private int best;
    private int average;
    private int value1;
    private int value2;
    private int value3;
    private int value4;
    private int value5;

    private String regionalSingleRecord;
    private String regionalAverageRecord;

    public String getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(String competitionId) {
        this.competitionId = competitionId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getRoundId() {
        return roundId;
    }

    public void setRoundId(String roundId) {
        this.roundId = roundId;
    }

    public String getFormatId() {
        return formatId;
    }

    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonCountryId() {
        return personCountryId;
    }

    public void setPersonCountryId(String personCountryId) {
        this.personCountryId = personCountryId;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getBest() {
        return best;
    }

    public void setBest(int best) {
        this.best = best;
    }

    public int getAverage() {
        return average;
    }

    public void setAverage(int average) {
        this.average = average;
    }

    public int getValue1() {
        return value1;
    }

    public void setValue1(int value1) {
        this.value1 = value1;
    }

    public int getValue2() {
        return value2;
    }

    public void setValue2(int value2) {
        this.value2 = value2;
    }

    public int getValue3() {
        return value3;
    }

    public void setValue3(int value3) {
        this.value3 = value3;
    }

    public int getValue4() {
        return value4;
    }

    public void setValue4(int value4) {
        this.value4 = value4;
    }

    public int getValue5() {
        return value5;
    }

    public void setValue5(int value5) {
        this.value5 = value5;
    }

    public String getRegionalSingleRecord() {
        return regionalSingleRecord;
    }

    public void setRegionalSingleRecord(String regionalSingleRecord) {
        this.regionalSingleRecord = regionalSingleRecord;
    }

    public String getRegionalAverageRecord() {
        return regionalAverageRecord;
    }

    public void setRegionalAverageRecord(String regionalAverageRecord) {
        this.regionalAverageRecord = regionalAverageRecord;
    }
}
