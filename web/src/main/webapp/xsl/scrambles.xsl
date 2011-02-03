<?xml version="1.0" encoding="utf-8"?>

<!-- Copyright (C) 2010 Mads Mohr Christensen, <hr.mohr@gmail.com> This program 
	is free software: you can redistribute it and/or modify it under the terms 
	of the GNU General Public License as published by the Free Software Foundation, 
	either version 3 of the License, or (at your option) any later version. This 
	program is distributed in the hope that it will be useful, but WITHOUT ANY 
	WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
	FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. 
	You should have received a copy of the GNU General Public License along with 
	this program. If not, see <http://www.gnu.org/licenses/>. -->

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format"
	xmlns:java="http://xml.apache.org/xslt/java" exclude-result-prefixes="java"
	xmlns:scrambles="dk.cubing.liveresults.xalan.ScramblesExt">
	<xsl:output method="xml" indent="yes" />

	<xsl:template match="scrambles">
		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4-portrait" page-height="29.7cm" page-width="21.0cm" margin="0.25cm">
					<fo:region-body margin-top="1.75cm" margin-bottom="0.5cm" />
					<fo:region-before precedence="true" extent="1.75cm" />
					<fo:region-after precedence="true" extent="0.5cm" />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="A4-portrait" initial-page-number="1">
				<fo:static-content flow-name="xsl-region-before">
					<fo:block background-color="#00C0C0" border-style="solid" border-width="2pt" border-color="#000000" border-collapse="collapse">
						<fo:table table-layout="fixed" width="21.0cm">
							<fo:table-column column-width="14.5cm"/>
							<fo:table-column column-width="6.5cm"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell display-align="center" padding-left="0.25cm">
										<fo:block text-align="start" font-size="20pt">
											<xsl:value-of select="/scrambles/competitionName" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell padding-right="0.25cm">
										<fo:table table-layout="fixed" width="5.75cm">
									        <fo:table-column column-width="1.5cm"/>
									        <fo:table-column column-width="4.25cm"/>
											<fo:table-body>
												<fo:table-row>
													<fo:table-cell>
														<fo:block text-align="start">Event:</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:retrieve-marker retrieve-class-name="eventName" retrieve-position="first-including-carryover" retrieve-boundary="page"/>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell>
														<fo:block text-align="start">Round:</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:retrieve-marker retrieve-class-name="roundName" retrieve-position="first-including-carryover" retrieve-boundary="page"/>
													</fo:table-cell>
												</fo:table-row>
												<fo:table-row>
													<fo:table-cell>
														<fo:block text-align="start">Group:</fo:block>
													</fo:table-cell>
													<fo:table-cell>
														<fo:retrieve-marker retrieve-class-name="groupNumber" retrieve-position="first-including-carryover" retrieve-boundary="page"/>
													</fo:table-cell>
												</fo:table-row>
											</fo:table-body>
										</fo:table>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:static-content>
			
				<fo:static-content flow-name="xsl-region-after">
					<fo:block background-color="#00C0C0" border-style="solid" border-width="2pt" border-color="#000000" border-collapse="collapse">
						<fo:table table-layout="fixed" inline-progression-dimension="100%">
							<fo:table-column column-width="50%"/>
							<fo:table-column column-width="50%"/>
							<fo:table-body>
								<fo:table-row>
									<fo:table-cell>
										<fo:block text-align="start" font-size="10pt">
											Generated <xsl:value-of select="java:format(java:java.text.SimpleDateFormat.new('EEE, d MMM yyyy HH:mm:ss z'), java:java.util.Date.new())" />
										</fo:block>
									</fo:table-cell>
									<fo:table-cell>
										<fo:block text-align="end" font-size="10pt">
											Page <fo:page-number/> of <fo:page-number-citation ref-id="last-page"/>
			      						</fo:block>
									</fo:table-cell>
								</fo:table-row>
							</fo:table-body>
						</fo:table>
					</fo:block>
				</fo:static-content>
				
				<fo:flow flow-name="xsl-region-body">
					<xsl:apply-templates select="events/event" />
					<fo:block id="last-page" />
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>

	<xsl:template match="event">
		<fo:block break-before="page">
			<fo:marker marker-class-name="eventName">
				<fo:block text-align="end">
					<xsl:value-of select="name" />
				</fo:block>
			</fo:marker>
			<fo:marker marker-class-name="roundName">
				<fo:block text-align="end">
					<xsl:value-of select="round" />
				</fo:block>
			</fo:marker>
			<fo:marker marker-class-name="groupNumber">
				<fo:block text-align="end">
					<xsl:value-of select="group" />
				</fo:block>
			</fo:marker>
			<fo:block border-style="solid" border-width="1pt" border-color="#000000" border-collapse="collapse">
				<xsl:copy-of select="scrambles:generate(@variant, @unitSize, @numberOfScrambles, @sameScramble)" />
			</fo:block>
		</fo:block>
	</xsl:template>

</xsl:stylesheet>