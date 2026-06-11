import type { Preset } from 'unocss'
import { FileSystemIconLoader } from '@iconify/utils/lib/loader/node-loaders'

// https://www.npmjs.com/package/@uni-helper/unocss-preset-uni
import { presetUni } from '@uni-helper/unocss-preset-uni'
// @see https://unocss.dev/presets/legacy-compat
import { presetLegacyCompat } from '@unocss/preset-legacy-compat'
import {
  defineConfig,
  presetIcons,
  transformerDirectives,
  transformerVariantGroup,
} from 'unocss'

export default defineConfig({
  presets: [
    presetUni({
      attributify: false,
    }),
    presetIcons({
      scale: 1.2,
      warn: true,
      extraProperties: {
        'display': 'inline-block',
        'vertical-align': 'middle',
      },
      collections: {
        carbon: () => import('@iconify-json/carbon/icons.json').then(i => i.default || i),
        'my-icons': FileSystemIconLoader(
          './src/static/my-icons',
          (svg) => {
            let svgStr = svg
            svgStr = svgStr.includes('fill="')
              ? svgStr
              : svgStr.replace(/^<svg /, '<svg fill="currentColor" ')
            svgStr = svgStr
              .replace(/(<svg.*?width=)"(.*?)"/, '$1"1em"')
              .replace(/(<svg.*?height=)"(.*?)"/, '$1"1em"')
            return svgStr
          },
        ),
      },
    }),
    presetLegacyCompat({
      commaStyleColorFunction: true,
      legacyColorSpace: true,
    }) as Preset,
  ],
  transformers: [
    transformerDirectives(),
    transformerVariantGroup(),
  ],
  shortcuts: [
    {
      center: 'flex justify-center items-center',
    },
  ],
  safelist: [
    'i-carbon-home',
    'i-carbon-user',
    'i-carbon-book',
    'i-carbon-chart-line',
    'i-carbon-volume-up',
    'i-carbon-document',
    'i-carbon-task-complete',
    'i-carbon-star',
    'i-carbon-chevron-right',
    'i-carbon-chevron-left',
    'i-carbon-close',
    'i-carbon-restart',
    'i-carbon-volume-mute',
    'i-carbon-play',
    'i-carbon-pause',
    'i-carbon-settings',
    'i-carbon-notification',
    'i-carbon-information',
    'i-carbon-help',
    'i-carbon-logout',
    'i-carbon-moon',
    'i-carbon-parent-child',
    'i-carbon-security',
    'i-carbon-edit',
    'i-carbon-trophy',
    'i-carbon-error',
    'i-carbon-checkmark',
    'i-carbon-headphones',
    'i-carbon-idea',
    'i-carbon-tree',
  ],
  rules: [
    [
      'p-safe',
      {
        padding:
          'env(safe-area-inset-top) env(safe-area-inset-right) env(safe-area-inset-bottom) env(safe-area-inset-left)',
      },
    ],
    ['pt-safe', { 'padding-top': 'env(safe-area-inset-top)' }],
    ['pb-safe', { 'padding-bottom': 'env(safe-area-inset-bottom)' }],
  ],
  theme: {
    colors: {
      primary: '#4A90E2',
      secondary: '#FF9F43',
      success: '#2ECC71',
      appbg: '#F8FAFC',
      textMain: '#333333',
      textSub: '#666666',
      textLight: '#999999',
      borderLight: '#E5E7EB',
    },
    fontSize: {
      '2xs': ['20rpx', '28rpx'],
      '3xs': ['18rpx', '26rpx'],
    },
  },
})
