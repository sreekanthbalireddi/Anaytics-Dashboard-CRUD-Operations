#!/usr/bin/env bash
# Some Selenium setups hardcode webdriver.chrome.driver=/usr/local/chromedriver_mac on macOS.
# This links that path to the newest chromedriver cached by WebDriverManager under ~/.cache/selenium/.
#
#   chmod +x scripts/link-chromedriver-mac.sh
#   ./scripts/link-chromedriver-mac.sh

set -euo pipefail

TARGET="/usr/local/chromedriver_mac"
CACHE_ROOT="${HOME}/.cache/selenium/chromedriver"

if [[ ! -d "${CACHE_ROOT}" ]]; then
  echo "No cache at ${CACHE_ROOT}. Run the test once so WebDriverManager downloads a driver, then run this script again."
  exit 1
fi

DRIVER=$(find "${CACHE_ROOT}" -name chromedriver -type f 2>/dev/null | sort -r | head -1)
if [[ -z "${DRIVER}" ]]; then
  echo "No chromedriver binary found under ${CACHE_ROOT}."
  exit 1
fi

echo "Linking ${TARGET} -> ${DRIVER}"
sudo mkdir -p /usr/local
sudo ln -sf "${DRIVER}" "${TARGET}"
echo "Done. Re-run your TestNG suite."
