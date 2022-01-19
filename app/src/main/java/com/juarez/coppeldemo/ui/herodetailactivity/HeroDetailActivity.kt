package com.juarez.coppeldemo.ui.herodetailactivity

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.juarez.coppeldemo.databinding.ActivityHeroDetailBinding
import com.juarez.coppeldemo.extensions.convertToInt
import com.juarez.coppeldemo.extensions.loadImage
import com.juarez.coppeldemo.repositories.HeroRepository
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.utils.HeroViewModelFactory

class HeroDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHeroDetailBinding
    private lateinit var viewModel: HeroDetailViewModel
    private var heroId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val provider = HeroViewModelFactory(HeroRepository())
        viewModel = ViewModelProvider(this, provider).get(HeroDetailViewModel::class.java)

        heroId = intent.getIntExtra(Constants.EXTRA_HERO_ID, 0)
        val heroUrl = intent.getStringExtra(Constants.EXTRA_HERO_URL)
        binding.imgDetailPhoto.loadImage(heroUrl)

        viewModel.getHeroDetail(heroId)
        viewModel.hero.observe(this, {
            with(binding) {
                with(it.powerStats) {
                    progressIntelligence.progress = intelligence?.convertToInt() ?: 0
                    progressStrength.progress = strength?.convertToInt() ?: 0
                    progressSpeed.progress = speed?.convertToInt() ?: 0
                    progressDurability.progress = durability?.convertToInt() ?: 0
                    progressPower.progress = power?.convertToInt() ?: 0
                    progressCombat.progress = combat?.convertToInt() ?: 0
                }
                with(it.biography) {
                    txtBioName.text = name ?: Constants.NO_AVAILABLE
                    txtFullName.text =
                        if (fullName.isNullOrEmpty()) Constants.NO_AVAILABLE else fullName
                    txtPlaceBirth.text = placeOfBirth ?: Constants.NO_AVAILABLE
                    txtAppearance.text = firstAppearance ?: Constants.NO_AVAILABLE
                    txtPublisher.text = publisher ?: Constants.NO_AVAILABLE
                    txtAlignment.text = alignment ?: Constants.NO_AVAILABLE
                }
                with(it.appearance) {
                    txtGender.text = gender ?: Constants.NO_AVAILABLE
                    txtRace.text = race ?: Constants.NO_AVAILABLE
                    txtHeight.text = height?.toString() ?: Constants.NO_AVAILABLE
                    txtWeight.text = weight?.toString() ?: Constants.NO_AVAILABLE
                    txtEyeColor.text = eyeColor ?: Constants.NO_AVAILABLE
                    txtHairColor.text = hairColor ?: Constants.NO_AVAILABLE
                }
                with(it.connections) {
                    txtAffiliations.text = groupAffiliation ?: Constants.NO_AVAILABLE
                    txtRelatives.text = relatives ?: Constants.NO_AVAILABLE
                }
            }
        })
        viewModel.loading.observe(this, {
            binding.progressBarHeroDetail.isVisible = it
        })
        viewModel.error.observe(this, {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Aviso")
            builder.setMessage(it)
            builder.setPositiveButton("Reintentar") { dialog, _ ->
                viewModel.getHeroDetail(heroId)
                dialog.dismiss()
            }
            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        })

    }
}
