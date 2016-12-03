package ru.spbau.mit.painter

import ru.spbau.mit.artefacts.MedicineChest
import ru.spbau.mit.artefacts.Shield
import ru.spbau.mit.artefacts.Sword
import ru.spbau.mit.creatures.Goblin
import ru.spbau.mit.creatures.Scavenger
import ru.spbau.mit.creatures.Player
import ru.spbau.mit.world.ArrayMap
import ru.spbau.mit.world.World

/**
 * Class implements drawing in ASCII style
 */
class ASCIIPainter : Painter {
    private val canvas: MutableList<MutableList<Char>> = mutableListOf()
    private var info: String = ""

    override fun clear() = canvas.clear()

    override fun show() {
        canvas.forEach { println(it.joinToString(separator = "")) }
        println(info)
    }

    override fun draw(artifact: MedicineChest, params: DrawingParameters?) = draw('m', params)

    override fun draw(artifact: Shield, params: DrawingParameters?) = draw('h', params)

    override fun draw(artifact: Sword, params: DrawingParameters?) = draw('s', params)

    override fun draw(creature: Goblin, params: DrawingParameters?) = draw('G', params)

    override fun draw(creature: Player, params: DrawingParameters?) {
        draw('@', params)

        info = "HP: ${creature.hp}/${creature.maxHp}\tHP_GEN: ${creature.hpGenerationSpeed}\n"
        info += "DAMAGE: ${creature.damage}\tARMOR: ${creature.armor}\n"
        info += "ARTIFACTS: ${creature.artifacts.joinToString { it.javaClass.simpleName }}\n"
        info += "ACTIVATED ARTIFACTS: ${creature.activatedArtifacts.joinToString { it.javaClass.simpleName }}"
    }

    override fun draw(creature: Scavenger, params: DrawingParameters?) = draw('S', params)

    override fun draw(map: ArrayMap, params: DrawingParameters?) {
        map.walls.forEach { canvas.add(it.map { if (it) '#' else '.' }.toMutableList()) }
    }

    override fun draw(world: World, params: DrawingParameters?) {
        world.map.draw(this)
        world.creatures.forEach { pos, creature -> creature.draw(this, DrawingParameters(pos)) }
        world.artifacts.forEach { pos, artifacts -> artifacts.forEach { it.draw(this, DrawingParameters(pos)) } }
    }

    /**
     * Tries to put character in the given position
     */
    private fun draw(character: Char, params: DrawingParameters?) {
        if (params == null) {
            throw NullPointerException("DrawingParameters is null")
        }

        if (canvas.getOrNull(params.x) == null || canvas[params.x].getOrNull(params.y) == null) {
            throw IndexOutOfBoundsException("Impossible to draw character with $params")
        }

        canvas[params.x][params.y] = character
    }
}
