plugins {
    id("dev.kikugie.stonecutter")
}

stonecutter active "1.21.3"

// See https://stonecutter.kikugie.dev/wiki/config/params
stonecutter parameters {
    swaps["mod_version"] = "\"${property("mod.version")}\";"
    swaps["minecraft"] = "\"${node.metadata.version}\";"
    constants["release"] = property("mod.id") != "template"
    dependencies["fapi"] = node.project.property("deps.fabric_api") as String

    replacements {
        string(current.parsed >= "1.21.11") {
            replace("ResourceLocation", "Identifier")
            // to reflect the differences between v2 and the fork of v1 used on SMPOnline
            replace("CUSTOM_COLOR_RADIUS", "customColorRadius")
            replace("CUSTOM_COLOR_CENTERS", "customColorCenters")
        }

        string(current.parsed >= "26.1") {
            replace("classTweaker v2 named", "classTweaker v2 official")
        }
    }
}
