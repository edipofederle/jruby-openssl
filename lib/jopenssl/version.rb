module Jopenssl
  VERSION = '0.9.20.dev'
  BOUNCY_CASTLE_VERSION = '1.55'
  # @deprecated
  module Version
    # @private
    VERSION = Jopenssl::VERSION
    # @private
    BOUNCY_CASTLE_VERSION = Jopenssl::BOUNCY_CASTLE_VERSION
  end
end
