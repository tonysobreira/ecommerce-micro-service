import fs from 'fs';
import path from 'path';

const envFile = process.env.ENV_FILE || process.argv[2] || '.env.local';
const envPath = path.resolve(process.cwd(), envFile);

if (!fs.existsSync(envPath)) {
    console.error(`Environment file not found: ${envPath}`);
    process.exit(1);
}

const raw = fs.readFileSync(envPath, 'utf-8');
const parsed = {};

for (const line of raw.split(/\r?\n/)) {
    const trimmed = line.trim();
    if (!trimmed || trimmed.startsWith('#')) continue;
    const sepIndex = trimmed.indexOf('=');
    if (sepIndex === -1) continue;

    const key = trimmed.slice(0, sepIndex).trim();
    const value = trimmed.slice(sepIndex + 1).trim();
    parsed[key] = value;
}

const apiUrl = parsed.API_URL || 'http://localhost:8080';

const output = `export const environment = {\n    apiUrl: '${apiUrl}'\n};\n`;

const outputPath = path.resolve(process.cwd(), 'src/environments/environment.ts');
fs.writeFileSync(outputPath, output);

console.log(`Generated ${outputPath} from ${envFile}`);
