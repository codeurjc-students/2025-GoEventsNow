import { defineConfig } from 'vitest/config'

export default defineConfig({
  test: {
    coverage: {
      provider: 'v8', 
      reporter: ['text', 'json', 'html'],
      exclude: ['**/*.spec.ts', '**/node_modules/**', '**/test-setup.ts', '**/main.ts', '**/environment*.ts'], 
      include: ['src/**/*.{ts,html}'], 
      reportsDirectory: './coverage',
    },
  },
})