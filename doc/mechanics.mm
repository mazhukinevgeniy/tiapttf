<map version="1.0.1">
<!-- To view this file, download free mind mapping software FreeMind from http://freemind.sourceforge.net -->
<node CREATED="1475710827221" ID="ID_1054320100" MODIFIED="1475710835290" TEXT="mechanics">
<node CREATED="1475710856622" ID="ID_1986167927" MODIFIED="1475710860114" POSITION="right" TEXT="scoring">
<node CREATED="1475711365185" ID="ID_1033335223" MODIFIED="1475711369150" TEXT="comboScore">
<node CREATED="1475710861662" ID="ID_17024119" MODIFIED="1475710903649" TEXT="comboScore = base * tier * tier"/>
<node CREATED="1475710904197" ID="ID_1405463392" MODIFIED="1475710906273" TEXT="base = 5"/>
<node CREATED="1475711375098" ID="ID_1887990613" MODIFIED="1475711397333" TEXT="tier is determined by the number of blocks and +tier modificators"/>
<node CREATED="1475711426521" ID="ID_1292421529" MODIFIED="1475711448389" TEXT="minlenght combo, no upgrades &lt;=&gt; tier 1"/>
</node>
<node CREATED="1475711319730" ID="ID_1705955877" MODIFIED="1475711460629" TEXT="comboPack">
<node CREATED="1475711462008" ID="ID_1583270156" MODIFIED="1475711480101" TEXT="this is what increments real score"/>
<node CREATED="1475711518593" ID="ID_1760659798" MODIFIED="1475711521733" TEXT="globalMultiplier = linker.lastGameState().globalMultiplier();"/>
<node CREATED="1475711539985" ID="ID_1212384110" MODIFIED="1475711633684" TEXT="packScore = sum(comboScores) * packMultiplier * globalMultiplier"/>
</node>
<node CREATED="1475711649536" ID="ID_341022625" MODIFIED="1475711652803" TEXT="multiplier">
<node CREATED="1475711653710" ID="ID_924059808" MODIFIED="1475711655915" TEXT="global">
<node CREATED="1475711848798" ID="ID_1818858650" MODIFIED="1475711927723" TEXT="increased by packMultiplier every time pack is scored"/>
<node CREATED="1475711932397" ID="ID_555831852" MODIFIED="1475711934881" TEXT="decay?"/>
</node>
<node CREATED="1475711656215" ID="ID_370999193" MODIFIED="1475711658699" TEXT="packMult">
<node CREATED="1475711851582" ID="ID_19546445" MODIFIED="1475711995737" TEXT="aka packTier, it&apos;s a sum of combo tiers"/>
<node CREATED="1475711997325" ID="ID_1186632827" MODIFIED="1475712030424" TEXT="&#x441;&#x447;&#x435;&#x442; &#x43a;&#x443;&#x431;&#x438;&#x447;&#x435;&#x441;&#x43a;&#x438;&#x439; &#x43e;&#x442; &#x442;&#x438;&#x440;&#x430;!!"/>
</node>
</node>
</node>
<node CREATED="1475712616008" ID="ID_938360386" MODIFIED="1475712634619" POSITION="right" TEXT="core rules">
<node CREATED="1475712635752" ID="ID_576788200" MODIFIED="1475712638875" TEXT="spawn">
<node CREATED="1475712639608" ID="ID_959591667" MODIFIED="1475712642972" TEXT="overfill">
<node CREATED="1475712643904" ID="ID_1916159058" MODIFIED="1475712665964" TEXT="if you have multiplier &gt; 1, random blocks will be removed and multiplier will be consumed"/>
<node CREATED="1475712666695" ID="ID_876062660" MODIFIED="1475712674283" TEXT="otherwise you will lose"/>
</node>
</node>
</node>
</node>
</map>
