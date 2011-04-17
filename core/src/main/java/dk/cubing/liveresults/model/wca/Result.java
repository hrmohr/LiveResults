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
@IdClass(ResultPK.class)
public class Result implements Serializable {

    @Id
    private String competitionId;
    @Id
    private String eventId;
    @Id
    private String roundId;
    private String formatId;
    @Id
    private String personId;
    private String personName;
    private String countryId;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competitionId", insertable=false, updatable=false)
    private Competition competition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId", insertable=false, updatable=false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roundId", insertable=false, updatable=false)
    private Round round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formatId", insertable=false, updatable=false)
    private Format format;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId", insertable=false, updatable=false)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "personId", insertable=false, updatable=false),
            @JoinColumn(name = "countryId", insertable=false, updatable=false)
    })
    private Person person;

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

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
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

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
