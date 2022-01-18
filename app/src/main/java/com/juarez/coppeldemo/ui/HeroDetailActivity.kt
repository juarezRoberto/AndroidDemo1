package com.juarez.coppeldemo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.juarez.coppeldemo.databinding.ActivityHeroDetailBinding
import com.juarez.coppeldemo.extensions.convertToInt
import com.juarez.coppeldemo.extensions.loadImage
import com.juarez.coppeldemo.repositories.HeroRepository
import com.juarez.coppeldemo.utils.Constants
import com.juarez.coppeldemo.viewmodels.HeroViewModel
import com.juarez.coppeldemo.viewmodels.HeroViewModelFactory

class HeroDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHeroDetailBinding
    private lateinit var viewModel: HeroViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val provider = HeroViewModelFactory(HeroRepository())
        viewModel = ViewModelProvider(this, provider).get(HeroViewModel::class.java)

        val heroId = intent.getIntExtra("hero_id", 0)
        val heroUrl = intent.getStringExtra("hero_url")
        binding.imgDetailPhoto.loadImage(heroUrl)

        viewModel.getHeroDetail(heroId)
        viewModel.heroe.observe(this, {
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
            }
        })
        viewModel.loading.observe(this, {
            binding.progressBarHeroDetail.isVisible = it
        })
        viewModel.error.observe(this, {

        })

    }
}
