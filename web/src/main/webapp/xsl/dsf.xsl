<?xml version="1.0" encoding="UTF-8"?>

	<!--
		Copyright (C) 2009 Mads Mohr Christensen, <hr.mohr@gmail.com> This
		program is free software: you can redistribute it and/or modify it
		under the terms of the GNU General Public License as published by the
		Free Software Foundation, either version 3 of the License, or (at your
		option) any later version. This program is distributed in the hope
		that it will be useful, but WITHOUT ANY WARRANTY; without even the
		implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
		PURPOSE. See the GNU General Public License for more details. You
		should have received a copy of the GNU General Public License along
		with this program. If not, see <http://www.gnu.org/licenses/>.
	-->

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format">
	<xsl:output method="xml" indent="yes" />
	<xsl:template match="/">
		<fo:root>
			<fo:layout-master-set>
				<fo:simple-page-master master-name="A4-portrait"
					page-height="29.7cm" page-width="21.0cm" margin="1cm">
					<fo:region-body />
				</fo:simple-page-master>
			</fo:layout-master-set>
			<fo:page-sequence master-reference="A4-portrait">
				<fo:flow flow-name="xsl-region-body" font-family="Verdana"
					font-size="20pt" font-weight="normal" color="black">
					<xsl:for-each select="diplomas/diploma">
						<fo:block break-before="page" border-style="solid"
							border-width="1pt" border-color="#000000" text-align="center">
							<fo:block border-style="solid" border-width="2pt"
								border-color="#000000" margin="1pt">
								<xsl:variable name="competitionName" select="/diplomas/competitionName" />
								<xsl:choose>
            						<xsl:when test="string-length($competitionName) > 28">
	            						<fo:block margin-top="1cm" font-size="36pt"
											font-weight="bold">
											<xsl:apply-templates select="/diplomas/competitionName" />
										</fo:block>
            						</xsl:when>
            						<xsl:otherwise>
            							<fo:block margin-top="1cm" font-size="48pt"
											font-weight="bold">
											<xsl:apply-templates select="/diplomas/competitionName" />
										</fo:block>
            						</xsl:otherwise>
           						</xsl:choose>
								<fo:block margin-top="2cm">
									<xsl:apply-templates select="rank" />
								</fo:block>
								<fo:block font-size="36pt">
									<xsl:apply-templates select="eventName" />
								</fo:block>
								<fo:block margin-top="1cm">event is</fo:block>
								<xsl:variable name="teamEvent" select="teamEvent" />
								<xsl:choose>
									<xsl:when test="$teamEvent = 'TRUE'">
										<fo:block font-size="30pt"><xsl:apply-templates select="competitor1Name" /></fo:block>
										<fo:block font-size="30pt"><xsl:apply-templates select="competitor2Name" /></fo:block>
										<fo:block margin-top="1cm">with a final result of</fo:block>
										<fo:block font-size="36pt" margin-bottom="2.5cm">
											<xsl:apply-templates select="result" />
										</fo:block>
									</xsl:when>
									<xsl:otherwise>
										<fo:block font-size="36pt">
											<xsl:apply-templates select="competitorName" />
										</fo:block>
										<fo:block margin-top="1cm">with a final result of</fo:block>
										<fo:block font-size="36pt" margin-bottom="3.5cm">
											<xsl:apply-templates select="result" />
										</fo:block>
									</xsl:otherwise>
								</xsl:choose>
								<fo:block>
									<fo:table>
										<fo:table-column column-width="50%" />
										<fo:table-column column-width="50%" />
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell>
													<fo:block>
														<fo:external-graphic
															src="url('servlet-context:/img/diplomas/dsf/dsf_logo.jpg')" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block>
														<fo:external-graphic
															src="url('servlet-context:/img/diplomas/dsf/WCAlogo_XL.jpg')" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:block>
								<fo:block margin-top="1cm" margin-bottom="2cm">
									<fo:table>
										<fo:table-column column-width="50%" />
										<fo:table-column column-width="50%" />
										<fo:table-header>
											<fo:table-row>
												<fo:table-cell>
													<fo:block font-weight="bold">Organizer</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block font-weight="bold">WCA Delegate</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-header>
										<fo:table-body>
											<fo:table-row>
												<fo:table-cell>
													<fo:block>
														<xsl:apply-templates select="/diplomas/organiser" />
													</fo:block>
												</fo:table-cell>
												<fo:table-cell>
													<fo:block>
														<xsl:apply-templates select="/diplomas/wcaDelegate" />
													</fo:block>
												</fo:table-cell>
											</fo:table-row>
										</fo:table-body>
									</fo:table>
								</fo:block>
							</fo:block>
						</fo:block>
					</xsl:for-each>
				</fo:flow>
			</fo:page-sequence>
		</fo:root>
	</xsl:template>
</xsl:stylesheet>
