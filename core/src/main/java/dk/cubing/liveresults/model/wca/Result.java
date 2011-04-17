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
    @JoinColumn(name = "competitionId")
    private Competition competition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roundId")
    private Round round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formatId")
    private Format format;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "countryId")
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "personId"),
            @JoinColumn(name = "countryId")
    })
    private Person person;
}
