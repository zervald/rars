{
  inputs.nixpkgs.url = "github:nixos/nixpkgs/nixos-unstable";
  inputs.flake-utils.url = "github:numtide/flake-utils";
  outputs =
    {
      self,
      nixpkgs,
      flake-utils,
      ...
    }:
    let
      out =
        system:
        let
          pkgs = nixpkgs.legacyPackages.${system};
          appliedOverlay = self.overlays.default pkgs pkgs;
        in
        {
          packages.rarsm = appliedOverlay.rarsm;
          packages.default = appliedOverlay.rarsm;

          devShells.default = pkgs.mkShellNoCC {
            packages = with pkgs; [
              openjdk
              coreutils
              appliedOverlay.rarsm
            ];
          };
        };
    in
    flake-utils.lib.eachDefaultSystem out
    // {
      overlays.default = final: prev: {
        rarsm = final.callPackage ./package.nix { };
      };
    };
}
