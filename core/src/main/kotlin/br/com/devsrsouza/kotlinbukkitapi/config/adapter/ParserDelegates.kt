package br.com.devsrsouza.kotlinbukkitapi.config.adapter

import br.com.devsrsouza.kotlinbukkitapi.config.parser.*
import br.com.devsrsouza.kotlinbukkitapi.extensions.item.asMaterialData
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.material.MaterialData

fun location(
        default: Location,
        type: ParserType = ParserType.MAP
) = ObjectParserDelegate(default, LocationParser(type))

fun block(
        default: Block,
        type: ParserType = ParserType.MAP
) = ObjectParserDelegate(default, BlockParser(type))

fun chunk(
        default: Chunk,
        type: ParserType = ParserType.MAP
) = ObjectParserDelegate(default, ChunkParser(type))

fun materialData(
        default: MaterialData
) = ObjectParserDelegate(default, MaterialDataParser)

fun materialData(
        material: Material,
        data: Byte = 0
) = ObjectParserDelegate(material.asMaterialData(data), MaterialDataParser)

fun textComponent(
        default: TextComponent
) = ObjectParserDelegate(default, ComponentParser)