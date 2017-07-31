package net.protvino.entities

import javax.persistence.*
import java.time.LocalDateTime

@Entity
class Sms {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id
    LocalDateTime created
    LocalDateTime updated
    String receiver
    String text
    @Enumerated(EnumType.STRING)
    Status status

    @Override
    String toString() {
        final StringBuilder sb = new StringBuilder("Sms{")
        sb.append("id=").append(id)
        sb.append(", created=").append(created)
        sb.append(", updated=").append(updated)
        sb.append(", receiver='").append(receiver).append('\'')
        sb.append(", text='").append(text).append('\'')
        sb.append(", status=").append(status)
        sb.append('}')
        return sb.toString()
    }

    enum Status {
        /**
         * 0: Accepted for delivery
         */
        ACCEPTED,
        /**
         * 1: delivery success
         */
                SUCCESS,
        /**
         * 2: delivery failure
         */
                FAILURE,
        /**
         * 4: message buffered
         */
                BUFFERED,
        /**
         * 8: smsc submit
         */
                SUBMIT,
        /**
         * 16: smsc reject
         */
                REJECT,

        ERROR
    }
}
