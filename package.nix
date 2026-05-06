{
  lib,
  fetchurl,
  stdenvNoCC,
  makeWrapper,
  jre,
}:

stdenvNoCC.mkDerivation rec {
  pname = "rarsm";
  version = "1.7";

  src = fetchurl {
    url = "https://github.com/rarsm/rars/releases/download/v${version}/rars-${version}.jar";
    hash = "sha256-4SBg2Wg+bPUO/siQCqmsyiqGg4lnr4xj+RamYBi4hKQ=";
  };

  dontUnpack = true;

  nativeBuildInputs = [ makeWrapper ];

  installPhase = ''
    runHook preInstall
    export JAR=$out/share/java/${pname}/${pname}.jar
    install -D $src $JAR
    makeWrapper ${jre}/bin/java $out/bin/${pname} \
      --add-flags "-jar $JAR"
    runHook postInstall
  '';

  meta = {
    description = "RARSM -- RISC-V Assembler and Runtime Simulator (iMproved)";
    mainProgram = pname;
    homepage = "https://github.com/rarsm/rars";
    sourceProvenance = with lib.sourceTypes; [ binaryBytecode ];
    license = lib.licenses.mit;
    maintainers = [ ];
    platforms = lib.platforms.all;
  };
}
